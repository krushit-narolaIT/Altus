package com.krushit.service;

import com.krushit.entity.User;

import java.sql.SQLException;

public interface ICustomerService {
    String registerUser(User user) throws SQLException;
    User userLogin(String email, String password);
}
