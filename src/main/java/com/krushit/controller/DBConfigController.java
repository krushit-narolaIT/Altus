package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.utils.DBConnection;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;

public class DBConfigController extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

        String dbUrl = config.getServletContext().getInitParameter("db.url");
        String dbUsername = config.getServletContext().getInitParameter("db.username");
        String dbPassword = config.getServletContext().getInitParameter("db.password");
        String dbDriver = config.getServletContext().getInitParameter("db.driver");

        try {
            DBConnection.init(dbUrl, dbUsername, dbPassword, dbDriver);

            System.out.println(Message.DATABASE_CONNECTION_ESTABLISHED);
        } catch (Exception e) {
            System.out.println("failed...!!");
            System.exit(1);
            throw new ServletException(Message.DATABASE_CONNECTION_FAILED);
        }
    }
}
