package com.krushit.service;

import com.krushit.dao.UserDAO;
import com.krushit.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class CustomerService {
    private final UserDAO userDAO = new UserDAO();

    public String registerUser(User user) throws SQLException {

        user.setCreatedAt(LocalDateTime.now());
        return userDAO.registerUser(user);
    }

    public User userLogin(String email, String password) {
        return userDAO.userLogin(email, password);
    }
}
