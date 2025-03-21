package com.krushit.common.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class APILoaderConfig {
    private static final Properties properties = new Properties();
    static {
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
