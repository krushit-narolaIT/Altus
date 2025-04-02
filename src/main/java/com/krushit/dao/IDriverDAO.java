package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.Driver;

import java.util.List;

public interface IDriverDAO {
    boolean isDriverExist(int driverId) throws DBException;
    void updateDriveVerificationDetail(int driverId, boolean isVerified, String rejectionMessage) throws DBException;
    List<Driver> fetchAllDrivers() throws DBException;
    void insertDriverDetails(Driver driver) throws DBException;
    List<Driver> getDriversWithPendingVerification() throws DBException;
    int getDriverId(int userId) throws DBException;
    boolean isDocumentVerified(int driverId) throws DBException;
    boolean isDocumentExist(int driverId) throws DBException;
    boolean isLicenseNumberExist(String licenseNumber) throws DBException;
    String isDocumentUnderReview(int driverId) throws DBException;
    void updateDriverAvailability(int driverId) throws DBException;
}
