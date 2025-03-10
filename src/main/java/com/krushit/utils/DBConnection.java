package com.krushit.utils;

import com.krushit.exception.GenericException;
import jakarta.servlet.ServletContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String DB_URL;
    private static String DB_USER_NAME;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    static {
        try {
            ServletContext context = ContextListener.getServletContext();
            DB_URL = context.getInitParameter("db.url");
            DB_USER_NAME = context.getInitParameter("db.username");
            DB_PASSWORD = context.getInitParameter("db.password");
            DB_DRIVER = context.getInitParameter("db.driver");

            Class.forName(DB_DRIVER);
        } catch (Exception e) {
            try {
                throw new GenericException("Error loading database configuration");
            } catch (GenericException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
    }
}
