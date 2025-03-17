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
    private IUserDAO userDAO = new UserDAOImpl();
    private IDriverDAO driverDAO = new DriverDAOImpl();

    public void registerUser(User user) throws SQLException, ApplicationException, ClassNotFoundException {
        user.setCreatedAt(LocalDateTime.now());
        userDAO.registerUser(user);
    }

    public User userLogin(String email, String password) throws ApplicationException, SQLException, ClassNotFoundException {
        return userDAO.userLogin(email, password);
    }

    public List<User> getAllCustomers() throws DBException {
        return userDAO.fetchAllCustomers();
    }

    public List<Driver> getAllDrivers() throws DBException {
        return driverDAO.fetchAllDrivers();
    }
}
