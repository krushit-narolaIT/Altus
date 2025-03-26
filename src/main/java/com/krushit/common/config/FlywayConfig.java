package com.krushit.common.config;

import com.krushit.common.Message;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

@WebListener
public class FlywayConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:mysql://localhost:3306/altus",
                            "root", "password123#")
                    .locations("classpath:db/migration")
                    .baselineOnMigrate(true)
                    .baselineVersion("1")
                    .load();
            flyway.migrate();
            System.out.println(Message.Database.FLYWAY_SUCCESSFUL);
        } catch (ClassNotFoundException e) {
            System.out.println(Message.Database.DRIVER_NOT_FOUND);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(Message.Database.FLYWAY_FAILED);
            e.printStackTrace();
        }
    }
}
