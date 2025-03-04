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

    static {
        Properties prop = new Properties();
        String filePath = "D://Project//Altus//application.properties";

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            prop.load(fileInputStream);
            DB_URL = prop.getProperty("db.url");
            DB_USER_NAME = prop.getProperty("db.username");
            DB_PASSWORD = prop.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Error loading application.properties" + filePath, e);
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println(DB_URL + " || " + DB_USER_NAME);
        return DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
    }
}




/*package com.krushit.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final String DB_URL;
    private static final String DB_USER_NAME;
    private static final String DB_PASSWORD;

    static {
        Properties prop = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(new File("src\\main\\resources\\application.properties"))) {
            prop.load(fileInputStream);
            DB_URL = prop.getProperty("db.url");
            DB_USER_NAME = prop.getProperty("db.username");
            DB_PASSWORD = prop.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
    }
}*/

/*package com.krushit.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;*/

/*public class DBConnection {
    private static final String DB_URL;
    private static final String DB_USER_NAME;
    private static final String DB_PASSWORD;

    static {
        Properties prop = new Properties();
//        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
        try (FileInputStream fileInputStream = new FileInputStream("application.properties")) {
            if (fileInputStream == null) {
                throw new RuntimeException("Error loading database configuration: application.properties not found in classpath");
            }
            prop.load(fileInputStream);
            DB_URL = prop.getProperty("db.url");
            DB_USER_NAME = prop.getProperty("db.username");
            DB_PASSWORD = prop.getProperty("db.password");
        } catch (IOException e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASSWORD);
    }
}*/





