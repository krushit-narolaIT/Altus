package com.krushit.utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {
    private static ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        servletContext = event.getServletContext();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        servletContext = null;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }
}
