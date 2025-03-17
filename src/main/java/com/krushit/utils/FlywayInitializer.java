package com.krushit.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.flywaydb.core.Flyway;

public class FlywayInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Flyway flyway = Flyway.configure().load();
        flyway.migrate();
    }

//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        System.out.println("Flyway migration started");
//        Flyway flyway = Flyway.configure()
//                .dataSource("jdbc:mysql://localhost:3306/altus", "root", "password123#")
//                .load();
//        flyway.migrate();
//        System.out.println("Flyway migration completed successfully.");
//    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
