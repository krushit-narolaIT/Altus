package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.model.Driver;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.exception.DBException;
import com.krushit.exception.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private final IDriverDAO driverDAO = new DriverDAOImpl();
    private final IUserDAO userDAO = new UserDAOImpl();

    public User driverLogin(String email, String password) throws DBException {
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

    public boolean isDriverExist(Integer driverId) throws ApplicationException {
        return driverDAO.isDriverExist(driverId);
    }

    public void verifyDriver(Integer driverId, boolean isVerified, String rejectionMessage) throws ApplicationException {
        if (!isDriverExist(driverId)) {
            throw new ApplicationException(Message.DRIVER_NOT_EXIST);
        }
        driverDAO.verifyDriver(driverId, isVerified, rejectionMessage);
    }

    public List<Driver> getAllDrivers() throws DBException {
        return driverDAO.fetchAllDrivers();
    }
}