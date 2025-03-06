package com.krushit.service;

import com.krushit.dao.UserDAOImpl;
import com.krushit.entity.User;

import java.sql.SQLException;

public class CustomerServiceImpl implements ICustomerService {
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    public String registerUser(User user) throws SQLException {
        return userDAO.registerUser(user);
    }

    @Override
    public User userLogin(String email, String password) {
        return userDAO.userLogin(email, password);
    }
}
