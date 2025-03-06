package com.krushit.service;

import com.krushit.entity.Driver;
import com.krushit.entity.User;

import java.sql.SQLException;

public interface IDriverService {
    String registerDriver(User driver) throws SQLException;
    Driver driverLogin(String email, String password);
}
