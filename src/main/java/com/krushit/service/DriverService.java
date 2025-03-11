package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.dao.DriverDAO;
import com.krushit.dao.UserDAO;
import com.krushit.entity.Driver;
import com.krushit.entity.Role;
import com.krushit.entity.User;
import com.krushit.exception.DBException;
import com.krushit.exception.GenericException;

import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private final DriverDAO driverDAO = new DriverDAO();
    private final UserDAO userDAO = new UserDAO();

    public void registerDriver(User user) throws SQLException, DBException, GenericException {
        try {
            user.setRole(Role.ROLE_DRIVER);
            driverDAO.registerDriver(user);
        } catch (DBException e) {
            throw new DBException(e.getMessage());
        } catch (GenericException e){
            throw new GenericException(e.getMessage());
        }
    }

    public User driverLogin(String email, String password) {
        return driverDAO.driverLogin(email, password);
    }


    public void storeDriverDetails(Driver driver) throws DBException {
        int userId = driver.getUserId();

        User user = userDAO.getUserDetails(userId);
        if (user == null) {
            throw new DBException("User not found with ID: " + userId);
        }

        driver.setRole(user.getRole());
        driver.setFirstName(user.getFirstName());
        driver.setLastName(user.getLastName());
        driver.setPhoneNo(user.getPhoneNo());
        driver.setEmailId(user.getEmailId());
        driver.setPassword(user.getPassword());
        driver.setActive(user.isActive());
        driver.setDisplayId(user.getDisplayId());
        driver.setCreatedAt(user.getCreatedAt());
        driver.setUpdatedAt(user.getUpdatedAt());
        driver.setCreatedBy(user.getCreatedBy());
        driver.setUpdatedBy(user.getUpdatedBy());

        try {
            driverDAO.insertDriverDetails(driver);
        } catch (SQLException e) {
            throw new DBException(Message.DRIVER_ALREADY_EXIST);
        }
    }

    public List<Driver> getPendingVerificationDrivers() throws SQLException {
        System.out.println("In service");
        return driverDAO.getPendingVerificationDrivers();
    }
}