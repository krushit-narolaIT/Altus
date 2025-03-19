package com.krushit.utils;

import java.sql.*;

public class DBConnection {
    public static DBConnection INSTANCE = null;

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbDriver;

    private DBConnection(String url, String username, String password, String driver) {
        dbUrl = url;
        dbUsername = username;
        dbPassword = password;
        dbDriver = driver;
    }

    public static DBConnection getInstance(String url, String username, String password, String driver) throws SQLException, ClassNotFoundException {
        if (INSTANCE == null) {
            INSTANCE = new DBConnection(url,username,password,driver);
        }
        return INSTANCE;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.dbDriver);
        return DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
    }
}