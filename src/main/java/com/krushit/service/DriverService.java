package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.model.Driver;
import com.krushit.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class DriverService {
    private static final String STORAGE_PATH = "D:\\Project\\AltusDriverLicences";

    private IDriverDAO driverDAO = new DriverDAOImpl();
    private IUserDAO userDAO = new UserDAOImpl();

    public void storeDriverDetails(Driver driver) throws ApplicationException, SQLException {
        if (driverDAO.isLicenseNumberExists(driver.getLicenceNumber())) {
            throw new ApplicationException(Message.Driver.LICENCE_NUMBER_IS_ALREADY_EXIST);
        }

        User user = userDAO.getUserDetails(driver.getUserId());
        if (user == null) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        String photoPath = storeLicencePhoto(driver.getLicencePhoto(), driver.getLicenceNumber(), user.getDisplayId());
        Driver updatedDriver = (Driver) new Driver.DriverBuilder()
                .setLicenceNumber(driver.getLicenceNumber())
                .setLicencePhoto(photoPath)
                .setDriverId(driver.getDriverId())
                .setUserId(driver.getUserId())
                .setRole(user.getRole())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNo(user.getPhoneNo())
                .setEmailId(user.getEmailId())
                .setDisplayId(user.getDisplayId())
                .build();
        driverDAO.insertDriverDetails(updatedDriver);
    }


    private String storeLicencePhoto(String licencePhotoPath, String licenceNumber, String displayId) throws ApplicationException {
        try {
            File sourceFile = new File(licencePhotoPath);
            if (!sourceFile.exists()) {
                throw new ApplicationException(Message.Driver.LICENCE_PHOTO_PATH_IS_REQUIRD + licencePhotoPath);
            }
            File file = new File(STORAGE_PATH);
            if (!file.exists()) {
                file.mkdirs();
            }
            String extension = getFileExtension(sourceFile);
            String fileName = "DRI_" + licenceNumber + "_" + displayId + extension;
            Path path = Paths.get(STORAGE_PATH, fileName);
            Files.copy(sourceFile.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
            return path.toString();
        } catch (IOException e) {
            throw new DBException(Message.Driver.FAILED_TO_STORE_DOCUMENT + e.getMessage());
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public List<Driver> getPendingVerificationDrivers() throws SQLException, ClassNotFoundException, DBException {
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