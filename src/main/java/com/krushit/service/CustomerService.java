package com.krushit.service;

import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.model.Driver;
import com.krushit.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerService {
    private final IUserDAO userDAO = new UserDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();

    public void registerUser(User user) throws SQLException, ApplicationException {
        user.setCreatedAt(LocalDateTime.now());
        userDAO.registerUser(user);
    }

    public User userLogin(String email, String password) {
        System.out.println("In UserLogin");
        return userDAO.userLogin(email, password);
    }

    public List<User> getAllCustomers() {
        return userDAO.fetchAllCustomers();
    }

    public List<Driver> getAllDrivers() throws DBException {
        return driverDAO.fetchAllDrivers();
    }
}
