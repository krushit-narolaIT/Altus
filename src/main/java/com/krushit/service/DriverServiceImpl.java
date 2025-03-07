package com.krushit.service;

import com.krushit.dao.DriverDAO;
import com.krushit.entity.User;

import java.sql.SQLException;

public class DriverServiceImpl implements IDriverService {
    private final DriverDAO driverDAO = new DriverDAO();

    @Override
    public String registerDriver(User driver) throws SQLException {
        return driverDAO.registerDriver(driver);
    }

    @Override
    public User driverLogin(String email, String password) {
        return driverDAO.driverLogin(email, password);
    }
}