package Connectivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection connection;
    public Connection getConnection(){
        String dbName = "projectdb924";
        String username = "projectdb924";
        String password = "Od4gA95fZ7~?";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://den1.mysql4.gear.host/"+dbName,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
