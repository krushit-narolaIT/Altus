package com.krushit.service;

import com.krushit.dao.DriverDAOImpl;
import com.krushit.entity.Driver;
import com.krushit.entity.User;

import java.sql.SQLException;

public class DriverServiceImpl implements IDriverService {
    private final DriverDAOImpl driverDAO = new DriverDAOImpl();

    @Override
    public String registerDriver(User driver) throws SQLException {
        return driverDAO.registerDriver(driver);
    }

    @Override
    public Driver driverLogin(String email, String password) {
        return driverDAO.driverLogin(email, password);
    }
}