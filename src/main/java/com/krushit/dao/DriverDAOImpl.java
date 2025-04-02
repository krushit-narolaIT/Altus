package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.DriverDocumentVerificationStatus;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;
import com.krushit.model.Driver;
import com.krushit.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements IDriverDAO {
    private static final String IS_DOCUMENT_UNDER_REVIEW = "SELECT verification_status FROM drivers WHERE driver_id = ?";
    private static final String UPDATE_DRIVER_DETAILS = "UPDATE Drivers SET licence_number = ?, licence_photo = ?, updated_by = ? WHERE user_id = ?";
    private static final String GET_PENDING_VERIFICATION_DRIVERS = "SELECT * FROM drivers WHERE is_document_verified = FALSE AND licence_number IS NOT NULL";
    private static final String UPDATE_DRIVER_VERIFICATION_STATUS = "UPDATE drivers SET verification_status = ?, comment = ?, is_document_verified = ?, updated_by = ? WHERE driver_id = ?";
    private static final String CHECK_DRIVER_EXISTENCE = "SELECT 1 FROM drivers WHERE driver_id = ?";
    private static final String CHECK_DRIVER_DOCUMENTS = "SELECT licence_number FROM drivers WHERE driver_id = ?";
    private static final String GET_ALL_DRIVERS = "SELECT * FROM drivers";
    private static final String GET_DRIVER_ID_FROM_USERID = "SELECT driver_id FROM Drivers WHERE user_id = ?";
    private static final String IS_DOCUMENT_VERIFIED = "SELECT is_document_verified FROM Drivers WHERE driver_id = ?";
    private static final String IS_LICENCE_EXIST = "SELECT 1 FROM drivers WHERE licence_number = ?";
    private static final String UPDATE_DRIVER_AVAILABILITY = "UPDATE Drivers SET is_available = TRUE WHERE driver_id = ?";
    private static final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    public void insertDriverDetails(Driver driver) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DRIVER_DETAILS)) {
            statement.setString(1, driver.getLicenceNumber());
            statement.setString(2, driver.getLicencePhoto());
            statement.setString(3, Role.ROLE_DRIVER.getRoleName());
            statement.setInt(4, driver.getUserId());
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_INSERT_DRIVER_DETAILS, e);
        }
    }

    //TODO : Use join query to fetch data in single query
    @Override
    public List<Driver> getDriversWithPendingVerification() throws DBException {
        List<Driver> pendingDrivers = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PENDING_VERIFICATION_DRIVERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = userDAO.getUserDetails(resultSet.getInt("user_id")).get();
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

    @Override
    public boolean isDriverExist(int driverId) throws DBException {
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

    @Override
    public void updateDriveVerificationDetail(int driverId, boolean isVerified, String rejectionMessage) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DRIVER_VERIFICATION_STATUS)) {
            preparedStatement.setString(1, isVerified ? DriverDocumentVerificationStatus.ACCEPTED.getStatus() : DriverDocumentVerificationStatus.REJECTED.getStatus());
            preparedStatement.setString(2, rejectionMessage);
            preparedStatement.setBoolean(3, isVerified);
            preparedStatement.setInt(4, driverId);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_VERIFYING_DRIVER, e);
        }
    }

    @Override
    public List<Driver> fetchAllDrivers() throws DBException {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DRIVERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                User userDrive = userDAO.getUserDetails(userId).get();
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


    @Override
    public int getDriverId(int userId) throws DBException {
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
        return 0;
    }

    @Override
    public boolean isDocumentVerified(int driverId) throws DBException {
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

    @Override
    public boolean isDocumentExist(int driverId) throws DBException {
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

    @Override
    public boolean isLicenseNumberExist(String licenseNumber) throws DBException {
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

    @Override
    public String isDocumentUnderReview(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DOCUMENT_UNDER_REVIEW)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.getString("verification_status");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_LICENCE_NUMBER, e);
        }
    }

    @Override
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