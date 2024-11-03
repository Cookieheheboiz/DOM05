package com.example.phan_loai;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnector {
    public Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "my_library";
        String databaseUser = "root";
        String databasePassword = "123456";
        String url = "jdbc:mysql://localhost/" + databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();

        }
        return databaseLink;
    }
}

