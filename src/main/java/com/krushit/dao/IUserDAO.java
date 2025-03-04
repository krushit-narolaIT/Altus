package com.krushit.dao;


import com.krushit.entity.User;
import com.krushit.entity.UserBuilder;

public interface IUserDAO {
    boolean registerUser(User user);
    User getUserDetails(String email_id, String password);
}