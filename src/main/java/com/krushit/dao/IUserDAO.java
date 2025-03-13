package com.krushit.dao;

import com.krushit.exception.ApplicationException;
import com.krushit.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void registerUser(User user) throws ApplicationException, SQLException, ClassNotFoundException;
    User userLogin(String emailId, String password);
    boolean isUserExist(String emailID) throws SQLException, ClassNotFoundException;
    User getUserDetails(int userId);
    List<User> fetchAllCustomers();
}
