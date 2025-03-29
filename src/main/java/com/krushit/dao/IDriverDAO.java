package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.dto.DateRangeIncomeDTO;
import com.krushit.model.Driver;

import java.time.LocalDate;
import java.util.List;

public interface IDriverDAO {
    boolean isDriverExist(Integer driverId) throws DBException;
    void verifyDriver(Integer driverId, boolean isVerified, String rejectionMessage) throws DBException;
    List<Driver> fetchAllDrivers() throws DBException;
    void insertDriverDetails(Driver driver) throws DBException;
    List<Driver> getPendingVerificationDrivers() throws DBException;
    Integer getDriverIdFromUserId(int userId) throws DBException;
    boolean isDriverDocumentVerified(int driverId) throws DBException;
    boolean isDriverDocumentUploaded(int driverId) throws DBException;
    boolean isLicenseNumberExists(String licenseNumber) throws DBException;
    void updateDriverAvailability(int driverId) throws DBException;
    //DateRangeIncomeDTO getRideDetailsByDateRange(int driverId, LocalDate startDate, LocalDate endDate);
}
