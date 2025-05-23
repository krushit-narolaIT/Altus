package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.config.DBConfig;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.sql.SQLException;

@WebServlet(value = "/dbConfig", loadOnStartup = 1)
public class DBConfigController extends HttpServlet {
    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        try {
            String dbUrl = context.getInitParameter("db.url");
            String dbUsername = context.getInitParameter("db.username");
            String dbPassword = context.getInitParameter("db.password");
            String dbDriver = context.getInitParameter("db.driver");
            if (dbUrl == null || dbUsername == null || dbPassword == null || dbDriver == null) {
                throw new ApplicationException(Message.DATABASE_ERROR);
            }
            DBConfig.INSTANCE = DBConfig.getInstance(dbUrl, dbUsername, dbPassword, dbDriver);
            System.out.println(Message.Database.CONNECTION_SUCCESSFUL);
        } catch (ApplicationException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
