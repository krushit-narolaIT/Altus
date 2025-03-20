package com.krushit.dao;

import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void registerUser(User user) throws ApplicationException, SQLException, ClassNotFoundException;
    User userLogin(String emailId, String password) throws ApplicationException, SQLException, ClassNotFoundException;
    boolean isUserExistWithEmail(String emailID) throws SQLException, ClassNotFoundException;
    boolean isUserExistWithPhone(String phoneNo) throws SQLException, ClassNotFoundException;
    User getUserDetails(int userId) throws DBException;
//    User getUserDetails(int userId) throws DBException;
//    User getUserDetails(int userId) throws DBException;
    List<User> fetchAllCustomers() throws DBException;
}
