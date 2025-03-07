package com.krushit.dao;

import com.krushit.entity.User;

import java.sql.SQLException;

public interface IDriverDAO {
    String registerDriver(User driver) throws SQLException;
    User driverLogin(String email_id, String password);
    String getRole(int role_id);
    boolean isDriverExist(String emailID) throws SQLException;
}