package com.krushit.dao;


import com.krushit.entity.User;
import com.krushit.entity.UserBuilder;

import java.sql.SQLException;

public interface IUserDAO {
    String registerUser(User user) throws SQLException;
    User userLogin(String emailID, String password);
    String getRole(int roleID);
    boolean isUserExist(String emailID) throws SQLException;
}