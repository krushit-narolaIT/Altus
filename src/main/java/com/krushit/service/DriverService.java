package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.dto.DriverVerificationRequest;
import com.krushit.dto.DateRangeIncomeDTO;
import com.krushit.model.Driver;
import com.krushit.model.User;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DriverService {
    private static final String STORAGE_PATH = "D:\\Project\\AltusDriverLicences";

    private final IDriverDAO driverDAO = new DriverDAOImpl();
    private final IUserDAO userDAO = new UserDAOImpl();

    public void storeDriverDetails(Driver driver) throws ApplicationException {
        if (driverDAO.isLicenseNumberExists(driver.getLicenceNumber())) {
            throw new ApplicationException(Message.Driver.LICENCE_NUMBER_IS_ALREADY_EXIST);
        }
        Optional<User> userOpt = userDAO.getUserDetails(driver.getUserId());
        if (!userOpt.isPresent()) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        User user = userOpt.get();
        Driver updatedDriver = (Driver) new Driver.DriverBuilder()
                .setLicenceNumber(driver.getLicenceNumber())
                .setLicencePhoto(driver.getLicencePhoto())
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

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }

    public List<Driver> getPendingVerificationDrivers() throws DBException {
        return driverDAO.getPendingVerificationDrivers();
    }

    public boolean isDriverExist(Integer driverId) throws ApplicationException {
        return driverDAO.isDriverExist(driverId);
    }

    public void verifyDriver(DriverVerificationRequest verificationRequest) throws ApplicationException {
        int driverId = verificationRequest.getDriverId();
        if (!isDriverExist(verificationRequest.getDriverId())) {
            throw new ApplicationException(Message.DRIVER_NOT_EXIST);
        }
        if(!driverDAO.isDriverDocumentUploaded(driverId)){
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_UPLOADED);
        }
        if("ACCEPT".equalsIgnoreCase(verificationRequest.getVerificationStatus())){
            driverDAO.verifyDriver(verificationRequest.getDriverId(), true, null);
        } else if("REJECT".equalsIgnoreCase(verificationRequest.getVerificationStatus())) {
            driverDAO.verifyDriver(verificationRequest.getDriverId(), false, verificationRequest.getMessage());
        } else {
            throw new ApplicationException(Message.Driver.PLEASE_PERFORM_VALID_VERIFICATION_OPERATION);
        }
    }

    public List<Driver> getAllDrivers() throws ApplicationException {
        return driverDAO.fetchAllDrivers();
    }

/*    public DateRangeIncomeDTO getRideDetailsByDateRange(int driverId, LocalDate startDate, LocalDate endDate) {
        return driverDAO.getRideDetailsByDateRange(driverId, startDate, endDate);
    }*/
}