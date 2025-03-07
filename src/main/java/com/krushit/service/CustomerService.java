package com.krushit.service;

import com.krushit.dao.UserDAO;
import com.krushit.entity.User;

import java.sql.SQLException;

public class CustomerService {
    private final UserDAO userDAO = new UserDAO();

    public String registerUser(User user) throws SQLException {
        return userDAO.registerUser(user);
    }

    public User userLogin(String email, String password) {
        return userDAO.userLogin(email, password);
    }
}
