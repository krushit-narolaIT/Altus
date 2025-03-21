package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.User;

import java.util.List;

public interface IUserDAO {
    void registerUser(User user) throws DBException;
    User userLogin(String emailId, String password) throws DBException ;
    boolean isUserExist(String emailID, String phoneNo) throws DBException;
    boolean isValidUser(String emailID, String password) throws DBException;
    User getUserDetails(int userId) throws DBException;
    List<User> fetchAllCustomers() throws DBException;
}
