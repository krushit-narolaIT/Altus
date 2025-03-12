package com.krushit.service;

import com.krushit.dao.DriverDAO;
import com.krushit.dao.UserDAO;
import com.krushit.model.Driver;
import com.krushit.model.Role;
import com.krushit.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CustomerService {
    private final UserDAO userDAO = new UserDAO();
    private final DriverDAO driverDAO = new DriverDAO();

    public String registerUser(User user) throws SQLException {
        user.setRole(Role.ROLE_CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        return userDAO.registerUser(user);
    }

    public User userLogin(String email, String password) {
        return userDAO.userLogin(email, password);
    }

    public List<User> getAllCustomers() {
        return userDAO.fetchAllCustomers();
    }

    public List<Driver> getAllDrivers() {
        return driverDAO.fetchAllDrivers();
    }


}
