package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    void registerUser(User user) throws DBException;
    User userLogin(String emailId, String password) throws DBException ;
    boolean isUserExist(String emailID, String phoneNo) throws DBException;
    boolean isValidUser(String emailID, String password) throws DBException;
    User getUserDetails(int userId) throws DBException;
    List<User> fetchAllCustomers() throws DBException;
    String getUserDisplayIdById(int userId) throws DBException;
    String getUserFullNameById(int userId) throws DBException;
    void updateUser(User updatedUser) throws DBException;
    Optional<User> findByEmail(String email) throws DBException;
    void updatePassword(String email, String newPassword) throws DBException;
    void updateUserRating(int toUserId, int rating, Connection connection) throws DBException;
}
