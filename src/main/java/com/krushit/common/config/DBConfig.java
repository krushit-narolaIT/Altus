package com.krushit.common.config;

import java.sql.*;

public class DBConfig {
    public static DBConfig INSTANCE = null;

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbDriver;

    private DBConfig(String url, String username, String password, String driver) {
        dbUrl = url;
        dbUsername = username;
        dbPassword = password;
        dbDriver = driver;
    }

    public static DBConfig getInstance(String url, String username, String password, String driver) throws SQLException, ClassNotFoundException {
        if (INSTANCE == null) {
            INSTANCE = new DBConfig(url,username,password,driver);
        }
        return INSTANCE;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.dbDriver);
        return DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
    }
}