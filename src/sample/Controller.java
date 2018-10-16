package sample;

import Connectivity.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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

    public void initialize(){
        try{
            DatabaseConnection dc = new DatabaseConnection();
            Connection connection = dc.getConnection();
            String query = "SELECT * FROM customers";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            result_label.setAlignment(Pos.CENTER);
            result_label.setStyle("-fx-text-fill: #000000");
            result_label.setText("No content to display!");
        }

    }

    public void insertStuff() throws SQLException {
        int err = 0;
        DatabaseConnection dc = new DatabaseConnection();
        Connection connection = dc.getConnection();
        String stuff = stuff_input.getText();
        String person = person_input.getText();
        String count_str = count_input.getText();
        String country = country_input.getText();
        String[] inputList = {stuff,person,count_str,country};
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
                int count_int = Integer.parseInt(count_str);
                result_label.setAlignment(Pos.CENTER);
                result_label.setStyle("-fx-text-fill: #4bc63d");
                result_label.setText("Successfully inserted!");
                String query = "INSERT INTO `customers`(Name,Person,Count,Country)" +
                        " VALUES ('"+stuff+"','"+person+"','"+count_int+"','"+country+"')";
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                stuff_input.setText("");person_input.setText("");
                count_input.setText("");country_input.setText("");
            }
            catch (Exception e) {
                result_label.setAlignment(Pos.CENTER);
                result_label.setStyle("-fx-text-fill: #ff0004");
                result_label.setText("Count must be a number!");
                e.printStackTrace();
            }
        }
    }

    public void carryStuff(){

    }
}
