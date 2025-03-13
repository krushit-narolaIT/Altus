package com.krushit.dao;

import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.model.Driver;
import com.krushit.model.User;

import java.sql.SQLException;
import java.util.List;

public interface IDriverDAO {
    User driverLogin(String emailId, String password) throws DBException;
    String getRole(int roleId) throws DBException;
    boolean isDriverExist(String emailID) throws SQLException;
    boolean isDriverExist(Integer driverId) throws ApplicationException;
    void verifyDriver(Integer driverId, boolean isVerified, String rejectionMessage) throws DBException;
    List<Driver> fetchAllDrivers() throws DBException;
    List<Driver> fetchPendingVerificationDrivers() throws DBException;
    void insertDriverDetails(Driver driver) throws SQLException, DBException;
    List<Driver> getPendingVerificationDrivers() throws SQLException, ClassNotFoundException;
    Integer getDriverIdFromUserId(int userId) throws DBException;
    boolean isDriverDocumentVerified(int driverId) throws ApplicationException;
    boolean isDriverDocumentUploaded(int driverId) throws DBException;
}
