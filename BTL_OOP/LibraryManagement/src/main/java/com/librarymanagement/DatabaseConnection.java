package com.librarymanagement;

import java.sql.Connection;
import java.sql.DriverManager;


public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection getConnection() {
        String databaseName = "librarymanagement";
        String databaseUser = "root";
        String databasePassword = "chitogeABVs32";
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
