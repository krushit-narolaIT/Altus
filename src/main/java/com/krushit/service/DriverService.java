package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.dao.DriverDAO;
import com.krushit.dao.UserDAO;
import com.krushit.model.Driver;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.exception.DBException;
import com.krushit.exception.ApplicationException;

import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private final DriverDAO driverDAO = new DriverDAO();
    private final UserDAO userDAO = new UserDAO();

    public void registerDriver(User user) throws SQLException, DBException, ApplicationException {
        try {
            user.setRole(Role.ROLE_DRIVER);
            driverDAO.registerDriver(user);
        } catch (DBException e) {
            throw new DBException(e.getMessage());
        } catch (ApplicationException e){
            throw new ApplicationException(e.getMessage());
        }
    }

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

    public List<Driver> getAllDrivers() {
        return driverDAO.fetchAllDrivers();
    }
}