package com.krushit.service;

import com.krushit.dao.UserDAOImpl;
import com.krushit.entity.User;
import com.krushit.entity.UserBuilder;

public class UserServiceImpl implements IUserService{
    private final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    public boolean registerUser(User user) {
        return userDAO.registerUser(user);
    }

    @Override
    public User userLogin(String email, String password) {
        return userDAO.userLogin(email, password);
    }
}
