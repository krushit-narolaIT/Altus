package com.krushit.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String DB_URL;
    private static final String DB_USER_NAME;
    private static final String DB_PASSWORD;
    private static Connection connection;

    static {
        Properties prop = new Properties();
        String filePath = "D://Project//Altus//application.properties";

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            prop.load(fileInputStream);
            DB_URL = prop.getProperty("db.url");
            DB_USER_NAME = prop.getProperty("db.username");
            DB_PASSWORD = prop.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException("Error initializing database connection", e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
