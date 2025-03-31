package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.enums.DriverDocumentVerificationStatus;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dao.*;
import com.krushit.dto.DriverVerificationRequest;
import com.krushit.model.Driver;
import com.krushit.model.User;
import com.krushit.model.Vehicle;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

public class DriverService {
    private static final String STORAGE_PATH = "D:\\Project\\AltusDriverLicences";

    private final IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();
    private final UserService userService = new UserService();

    public void storeDriverDetails(Driver driver) throws ApplicationException {
        Optional<User> userOpt = userService.getUserDetails(driver.getUserId());
        if (!userOpt.isPresent()) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        if (driverDAO.isLicenseNumberExists(driver.getLicenceNumber())) {
            throw new ApplicationException(Message.Driver.LICENCE_NUMBER_IS_ALREADY_EXIST);
        }
        if (driverDAO.isDocumentUnderReview(driver.getDriverId()).equalsIgnoreCase(DriverDocumentVerificationStatus.PENDING.getStatus())) {
            throw new ApplicationException(Message.Driver.DOCUMENT_IS_UNDER_REVIEW);
        }
        Driver updatedDriver = (Driver) new Driver.DriverBuilder()
                .setLicenceNumber(driver.getLicenceNumber())
                .setLicencePhoto(driver.getLicencePhoto())
                .setVerificationStatus(DriverDocumentVerificationStatus.PENDING.getStatus())
                .setUserId(driver.getUserId())
                .build();
        driverDAO.insertDriverDetails(updatedDriver);
    }

    public String storeLicencePhoto(Part filePart, String licenceNumber, String displayId) throws ApplicationException {
        try {
            File file = new File(STORAGE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            String originalFileName = filePart.getSubmittedFileName();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = "DRI_" + licenceNumber + "_" + displayId + extension;
            Path path = Paths.get(STORAGE_PATH, fileName);
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
            }
            return path.toString();
        } catch (IOException e) {
            throw new DBException(Message.Driver.FAILED_TO_STORE_DOCUMENT + e.getMessage());
        }
    }

    public List<Driver> getPendingVerificationDrivers() throws DBException {
        return driverDAO.getPendingVerificationDrivers();
    }

    public boolean isDriverExist(Integer driverId) throws ApplicationException {
        return driverDAO.isDriverExist(driverId);
    }

    public void verifyDriver(DriverVerificationRequest verificationRequest, int driverId) throws ApplicationException {
        if (!isDriverExist(driverId)) {
            throw new ApplicationException(Message.DRIVER_NOT_EXIST);
        }
        if(!driverDAO.isDriverDocumentUploaded(driverId)){
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_UPLOADED);
        }
        if(DriverDocumentVerificationStatus.ACCEPTED.getStatus().equalsIgnoreCase(verificationRequest.getVerificationStatus())){
            driverDAO.verifyDriver(verificationRequest.getDriverId(), true, null);
        } else if(DriverDocumentVerificationStatus.REJECTED.getStatus().equalsIgnoreCase(verificationRequest.getVerificationStatus())) {
            driverDAO.verifyDriver(verificationRequest.getDriverId(), false, verificationRequest.getMessage());
        } else {
            throw new ApplicationException(Message.Driver.PLEASE_PERFORM_VALID_VERIFICATION_OPERATION);
        }
    }

    public List<Driver> getAllDrivers() throws ApplicationException {
        return driverDAO.fetchAllDrivers();
    }

    public void addVehicle(Vehicle vehicle, int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        if (!driverDAO.isDriverDocumentUploaded(driverId)) {
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_UPLOADED);
        }
        if (!driverDAO.isDriverDocumentVerified(driverId)) {
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_VERIFIED);
        }
        if (vehicleDAO.isDriverVehicleExist(driverId)) {
            throw new ApplicationException(Message.Vehicle.DRIVER_VEHICLE_ALREADY_EXIST);
        }
        if (!vehicleDAO.isBrandModelExistsByID(vehicle.getVehicleId())) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_NOT_SUPPORTED);
        }
        Integer minYear = vehicleDAO.getMinYearForBrandModel(vehicle.getBrandModelId());
        if (vehicle.getYear() < minYear) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_YEAR_NOT_SUPPORTED);
        }
        vehicle.setDriverId(driverId);
        driverDAO.updateDriverAvailability(driverId);
        vehicleDAO.addVehicle(vehicle);
    }

    public void deleteVehicle(int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        if (!vehicleDAO.isDriverVehicleExist(driverId)) {
            throw new ApplicationException(Message.Vehicle.VEHICLE_NOT_EXIST);
        }
        vehicleDAO.deleteVehicleByUserId(userId);
    }

    public int getDriverIdFromUserId(int userId) throws ApplicationException{
        return driverDAO.getDriverIdFromUserId(userId);
    }
}