package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface IUserDAO {
    void registerUser(User user) throws DBException;
    User getUser(String emailId, String password) throws DBException ;
    boolean isUserExist(String emailID, String phoneNo) throws DBException;
    boolean isValidUser(String emailID, String password) throws DBException;
    boolean isUserExist(int userId) throws DBException;
    Optional<User> getUser(int userId) throws DBException;
    List<User> getAllCustomers() throws DBException;
    String getUserDisplayId(int userId) throws DBException;
    String getUserFullName(int userId) throws DBException;
    void updateUser(User updatedUser) throws DBException;
    Optional<User> getUserByEmail(String email) throws DBException;
    void updatePassword(String email, String newPassword) throws DBException;
    void updateUserRating(int toUserId, int rating, Connection connection) throws DBException;
    void blockUser(int userId) throws DBException;
    boolean isUserBlocked(int userId) throws DBException;
    List<User> getUsersByLowRatingAndReviewCount(double ratingThreshold, int reviewCountThreshold) throws DBException;
}
