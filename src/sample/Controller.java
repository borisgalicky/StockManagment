package sample;

import Connectivity.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.TreeSet;

public class Controller {
    @FXML
    public Button carry_btn;
    @FXML
    public Button insert_btn;
    @FXML
    public TextField stuff_input;
    @FXML
    public TextField person_input;
    @FXML
    public TextField count_input;
    @FXML
    public TextField country_input;
    @FXML
    public Label result_label;
    @FXML
    public TableView<TableModel> table;
    @FXML
    public TableColumn<TableModel,String> name_column;
    @FXML
    public TableColumn<TableModel,String> person_column;
    @FXML
    public TableColumn<TableModel,Integer> count_column;
    @FXML
    public TableColumn<TableModel,String> country_column;

    ObservableList<TableModel> list = FXCollections.observableArrayList();

    public void initialize(){
        try{
            DatabaseConnection dc = new DatabaseConnection();
            Connection connection = dc.getConnection();
            String query = "SELECT * FROM customers";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            list.clear();
            while(rs.next()){
                String name = rs.getString("Name");
                String person = rs.getString("Person");
                int count = rs.getInt("Count");
                String country = rs.getString("Country");
                list.add(new TableModel(name,person,count,country));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        person_column.setCellValueFactory(new PropertyValueFactory<>("Person"));
        count_column.setCellValueFactory(new PropertyValueFactory<>("Count"));
        country_column.setCellValueFactory(new PropertyValueFactory<>("Country"));
        table.setItems(list);
    }

    int count_int;
    public void insertStuff() throws Exception {
        int err = 0;

        DatabaseConnection dc = new DatabaseConnection();
        Connection connection = dc.getConnection();
        Statement statement = connection.createStatement();
        String name = stuff_input.getText();
        String person = person_input.getText();
        String count = count_input.getText();
        String country = country_input.getText();

        String[] inputList = {name,person,count,country};
        for(String item : inputList){
            if(item.equals("")){
                err++;
            }
        }
        if(err>0){
            result_label.setAlignment(Pos.CENTER);
            result_label.setStyle("-fx-text-fill: #ff0004");
            result_label.setText("Fill all inputs!");
        } else {
            try{
                count_int = Integer.parseInt(count);
            } catch (Exception e){
                result_label.setAlignment(Pos.CENTER);
                result_label.setStyle("-fx-text-fill: #ff0004");
                result_label.setText("Count must be a number!");
                return;
            }
            String insert_query = "INSERT INTO customers (Name,Person,Count,Country) VALUES ('"+name+"','"+person+"','"+count_int+"','"+country+"')";
            String select_query = "SELECT ID,Count FROM customers WHERE name like '"+name+"' and person like '"+person+"' and country like '"+country+"'";
            ResultSet rs_sel = statement.executeQuery(select_query);
            if(rs_sel.next()){
                int newCount = rs_sel.getInt("Count")+count_int;
                statement.executeUpdate("UPDATE customers SET Count = '"+newCount+"'" +
                        " WHERE `customers`.`ID` = '"+rs_sel.getInt("ID")+"'");
                result_label.setAlignment(Pos.CENTER);
                result_label.setStyle("-fx-text-fill: #4bc63d");
                result_label.setText("Successfully inserted!");
                stuff_input.setText("");person_input.setText("");
                count_input.setText("");country_input.setText("");
                rs_sel.close();
            } else {
                statement.executeUpdate(insert_query);
                result_label.setAlignment(Pos.CENTER);
                result_label.setStyle("-fx-text-fill: #4bc63d");
                result_label.setText("Successfully inserted!");
                stuff_input.setText("");person_input.setText("");
                count_input.setText("");country_input.setText("");
            }
        }
        initialize();
    }

    public void carryStuff(){}
        /*int err = 0;
        String name = stuff_input.getText();
        String person = person_input.getText();
        String count = count_input.getText();
        String country = country_input.getText();

        String[] inputList = {name,person,count,country};
        for(String item : inputList){
            if(item.equals("")){
                err++;
            }
        } if(err>0){
            result_label.setAlignment(Pos.CENTER);
            result_label.setStyle("-fx-text-fill: #ff0004");
            result_label.setText("Fill all inputs!");
        } else {
            try{
                DatabaseConnection dc = new DatabaseConnection();
                Connection connection = dc.getConnection();

                String query1 = "SELECT Name FROM customers where Name like '"+name+"'";
                String query2 = "SELECT Person FROM customers where Name like '"+name+"'";
                String query3 = "SELECT Country FROM customers where Name like '"+name+"'";
                String query4 = "SELECT Count FROM customers where Name like '"+name+"'";

                Statement statement = connection.createStatement();
                ResultSet rs1 = statement.executeQuery(query1);
                if(rs1.next()){
                    rs1.close();
                    ResultSet rs2 = statement.executeQuery(query2);
                    while(rs2.next()){
                        if(rs2.getString("Person").equals(person)){//rs2.getString(columnLabel:"Person") sa rovná hodnote Boris, nie Martin
                            rs2.close();
                            ResultSet rs3 = statement.executeQuery(query3);
                            while(rs3.next()){
                                if(rs3.getString("Country").equals(country)){
                                    rs3.close();
                                    ResultSet rs4 = statement.executeQuery(query4);
                                    rs4.next();
                                    try{
                                        int count_int = Integer.parseInt(count);
                                        if(rs4.getInt("Count")==count_int){
                                            String del_query = "DELETE FROM customers WHERE Name like '"+name+"'" +
                                                    " and Person like '"+person+"' and Country like '"+country+"'";
                                            statement.executeUpdate(del_query);
                                            result_label.setAlignment(Pos.CENTER);
                                            result_label.setStyle("-fx-text-fill: #4bc63d");
                                            result_label.setText("Successfully carried!");
                                            stuff_input.setText("");person_input.setText("");
                                            count_input.setText("");country_input.setText("");
                                        } else if(rs4.getInt("Count")<count_int){
                                            result_label.setAlignment(Pos.CENTER);
                                            result_label.setStyle("-fx-text-fill: #ff0004");
                                            result_label.setText("Insufficient quantity of goods!");
                                        } else if((rs4.getInt("Count")>count_int)){
                                            int new_count = rs4.getInt("Count")-count_int;
                                            String up_query = "UPDATE customers SET Count = '"+new_count+"'" +
                                                    " WHERE customers.Name like '"+name+"' and customers.Person like" +
                                                    " '"+person+"' and customers.Country like '"+country+"'";
                                            statement.executeUpdate(up_query);
                                            result_label.setAlignment(Pos.CENTER);
                                            result_label.setStyle("-fx-text-fill: #4bc63d");
                                            result_label.setText("Successfully carried!");
                                            stuff_input.setText("");person_input.setText("");
                                            count_input.setText("");country_input.setText("");

                                        }
                                    } catch (Exception e){
                                        result_label.setAlignment(Pos.CENTER);
                                        result_label.setStyle("-fx-text-fill: #ff0004");
                                        result_label.setText("Count must be a number!");
                                        e.printStackTrace();
                                    }
                                } else {
                                    result_label.setAlignment(Pos.CENTER);
                                    result_label.setStyle("-fx-text-fill: #ff0004");
                                    result_label.setText("Origin country is incorrect!");
                                }
                            }
                        } else {
                            result_label.setAlignment(Pos.CENTER);
                            result_label.setStyle("-fx-text-fill: #ff0004");
                            result_label.setText("No such person to this stuff!");
                        }
                    }
                } else {
                    result_label.setAlignment(Pos.CENTER);
                    result_label.setStyle("-fx-text-fill: #ff0004");
                    result_label.setText("No such stuff!");
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        initialize();
    }*/
}
