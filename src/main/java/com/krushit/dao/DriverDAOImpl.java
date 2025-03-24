package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.Driver;
import com.krushit.model.User;
import com.krushit.common.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements IDriverDAO {
    private final String DRIVER_LOGIN = "SELECT * FROM users WHERE email_id = ? AND password = ?";
    private final String UPDATE_DRIVER_DETAILS = "UPDATE Drivers SET licence_number = ?, licence_photo = ? WHERE user_id = ?";
    private final String GET_PENDING_VERIFICATION_DRIVERS = "SELECT * FROM drivers WHERE is_document_verified = FALSE AND licence_number IS NOT NULL";
    private final String UPDATE_DRIVER_VERIFICATION_STATUS = "UPDATE drivers SET verification_status = ?, comment = ?, is_document_verified = ? WHERE driver_id = ?";
    private final String CHECK_DRIVER_EXISTENCE = "SELECT 1 FROM drivers WHERE driver_id = ?";
    private final String CHECK_DRIVER_DOCUMENTS = "SELECT licence_number FROM drivers WHERE driver_id = ?";
    private final String GET_ALL_DRIVERS = "SELECT * FROM drivers";
    private final String GET_DRIVER_ID_FROM_USERID = "SELECT driver_id FROM Drivers WHERE user_id = ?";
    private final String IS_DOCUMENT_VERIFIED = "SELECT is_document_verified FROM Drivers WHERE driver_id = ?";
    private final String IS_LICENCE_EXIST = "SELECT 1 FROM drivers WHERE licence_number = ?";
    private final String UPDATE_DRIVER_AVAILABILITY = "UPDATE Drivers SET is_available = TRUE WHERE driver_id = ?";

    private final UserDAOImpl userDAO = new UserDAOImpl();

    public boolean isDriverExist(String emailID) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(CHECK_DRIVER_EXISTENCE)) {
            checkStmt.setString(1, emailID);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_DRIVER_EXISTENCE, e);
        }
        return false;
    }

    public void insertDriverDetails(Driver driver) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DRIVER_DETAILS)) {
            statement.setString(1, driver.getLicenceNumber());
            statement.setString(2, driver.getLicencePhoto());
            statement.setInt(3, driver.getUserId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_INSERT_DRIVER_DETAILS, e);
        }
    }

    public List<Driver> getPendingVerificationDrivers() throws DBException {
        List<Driver> pendingDrivers = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PENDING_VERIFICATION_DRIVERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = userDAO.getUserDetails(resultSet.getInt("user_id"));
                Driver driver = (Driver) new Driver.DriverBuilder()
                        .setLicenceNumber(resultSet.getString("licence_number"))
                        .setLicencePhoto(resultSet.getString("licence_photo"))
                        .setDocumentVerified(resultSet.getBoolean("is_document_verified"))
                        .setVerificationStatus(resultSet.getString("verification_status"))
                        .setComment(resultSet.getString("comment"))
                        .setDriverId(resultSet.getInt("driver_id"))
                        .setUserId(resultSet.getInt("user_id"))
                        .setEmailId(user.getEmailId())
                        .setFirstName(user.getFirstName())
                        .setLastName(user.getLastName())
                        .setRole(user.getRole())
                        .setDisplayId(user.getDisplayId())
                        .build();
                pendingDrivers.add(driver);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_GETTING_PENDING_VERIFICATION_DRIVER, e);
        }
        return pendingDrivers;
    }

    public boolean isDriverExist(Integer driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_DRIVER_EXISTENCE)) {
            preparedStatement.setInt(1, driverId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_DRIVER_EXISTENCE, e);
        }
    }

    public void verifyDriver(Integer driverId, boolean isVerified, String rejectionMessage) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DRIVER_VERIFICATION_STATUS)) {
            preparedStatement.setString(1, isVerified ? "ACCEPTED" : "REJECTED");
            preparedStatement.setString(2, isVerified ? null : rejectionMessage);
            preparedStatement.setBoolean(3, isVerified);
            preparedStatement.setInt(4, driverId);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_VERIFYING_DRIVER, e);
        }
    }

    public List<Driver> fetchAllDrivers() throws DBException {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DRIVERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                User userDrive = userDAO.getUserDetails(userId);
                Driver driver = (Driver) new Driver.DriverBuilder()
                        .setDriverId(resultSet.getInt("driver_id"))
                        .setLicenceNumber(resultSet.getString("licence_number"))
                        .setDocumentVerified(resultSet.getBoolean("is_document_verified"))
                        .setAvailable(resultSet.getBoolean("is_available"))
                        .setVerificationStatus(resultSet.getString("verification_status"))
                        .setComment(resultSet.getString("comment"))
                        .setUserId(userId)
                        .setFirstName(userDrive.getFirstName())
                        .setLastName(userDrive.getLastName())
                        .setEmailId(userDrive.getEmailId())
                        .build();
                drivers.add(driver);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_FETCHING_ALL_DRIVERS, e);
        }
        return drivers;
    }


    public Integer getDriverIdFromUserId(int userId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DRIVER_ID_FROM_USERID)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("driver_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_FOR_GETTING_DRIVER_ID_FROM_USER_ID, e);
        }
        return null;
    }

    public boolean isDriverDocumentVerified(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DOCUMENT_VERIFIED)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_document_verified");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_FOR_CHECKING_DRIVER_DOCUMENT_VERIFIED, e);
        }
        return false;
    }

    public boolean isDriverDocumentUploaded(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CHECK_DRIVER_DOCUMENTS)) {
            stmt.setInt(1, driverId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String licenceNumber = resultSet.getString("licence_number");
                    return licenceNumber != null && !licenceNumber.trim().isEmpty();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_FOR_CHECKING_DRIVER_DOCUMENT_UPLOADED, e);
        }
        return false;
    }

    public boolean isLicenseNumberExists(String licenseNumber) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_LICENCE_EXIST)) {
            stmt.setString(1, licenseNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_LICENCE_NUMBER, e);
        }
    }

    public void updateDriverAvailability(int driverId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_DRIVER_AVAILABILITY)) {
            ps.setInt(1, driverId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_UPDATING_DRIVER_AVAILABILITY, e);
        }
    }
}