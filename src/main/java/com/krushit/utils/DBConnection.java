package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.exception.DBException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String DB_URL;
    private static String DB_USER_NAME;
    private static String DB_PASSWORD;
    private static String DB_DRIVER;

    public static void init(String url, String username, String password, String driver) throws Exception {
        DB_URL = url;
        DB_USER_NAME = username;
        DB_PASSWORD = password;
        DB_DRIVER = driver;

        if (DB_URL == null || DB_USER_NAME == null || DB_PASSWORD == null || DB_DRIVER == null) {
            throw new Exception("Missing database  credentials.");
        }

        Class.forName(DB_DRIVER);

        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new DBException(Message.DATABASE_CONNECTION_FAILED);
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
    }
}
