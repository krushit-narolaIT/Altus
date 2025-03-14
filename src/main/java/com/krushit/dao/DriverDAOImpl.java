package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.model.Driver;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.utils.DBConnection;
import com.krushit.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements IDriverDAO{
    private final String DRIVER_LOGIN = "SELECT * FROM users WHERE email_id = ? AND password = ?";
    private final String GET_ROLE = "SELECT role FROM roles WHERE role_id = ?";
    private final String INSERT_DRIVER_DETAILS = "INSERT INTO Drivers (user_id, licence_number, licence_photo) VALUES (?, ?, ?)";
    private final String UPDATE_DRIVER_DETAILS = "UPDATE Drivers SET licence_number = ?, licence_photo = ? WHERE user_id = ?";
    private final String GET_PENDING_VERIFICATION_DRIVERS = "SELECT * FROM drivers WHERE is_document_verified = FALSE";
    private final String UPDATE_DRIVER_VERIFICATION_STATUS = "UPDATE drivers SET verification_status = ?, comment = ? WHERE driver_id = ?";
    private final String CHECK_DRIVER_EXISTENCE = "SELECT 1 FROM drivers WHERE driver_id = ?";
    private final String CHECK_DRIVER_DOCUMENTS = "SELECT licence_number FROM drivers WHERE driver_id = ?";
    private final String GET_ALL_DRIVERS = "SELECT * FROM drivers";
    private final String GET_UNVERIFIED_DRIVER = "SELECT * FROM drivers WHERE document_verified = false";
    private final String GET_DRIVERID_FROM_USERID = "SELECT driver_id FROM Drivers WHERE user_id = ?";
    private final String IS_DOCUMENT_VERIFIED = "SELECT is_document_verified FROM Drivers WHERE driver_id = ?";

    private UserDAOImpl userDAO = new UserDAOImpl();

    public User driverLogin(String emailId, String password) throws DBException {
        User user = null;
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(DRIVER_LOGIN)) {
            statement.setString(1, emailId);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getInt("user_id"));
                    user.setRole(Role.getRole(resultSet.getInt("role_id")));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPhoneNo(resultSet.getString("phone_no"));
                    user.setEmailId(resultSet.getString("email_id"));
                    user.setDisplayId(resultSet.getString("display_id"));
                    user.setCreatedAt(DateUtils.toLocalDateTime(resultSet.getTimestamp("created_at")));
                    user.setUpdatedAt(DateUtils.toLocalDateTime(resultSet.getTimestamp("updated_at")));
                    user.setCreatedBy(resultSet.getString("created_by"));
                    user.setUpdatedBy(resultSet.getString("updated_by"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
        return user;
    }

    public String getRole(int role_id) throws DBException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ROLE)) {
            statement.setInt(1, role_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DRIVER_ALREADY_EXIST, e);
        }
        return null;
    }

    public boolean isDriverExist(String emailID) throws SQLException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(CHECK_DRIVER_EXISTENCE)) {
            checkStmt.setString(1, emailID);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertDriverDetails(Driver driver) throws DBException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_DRIVER_DETAILS)) {

            statement.setString(1, driver.getLicenceNumber());
            statement.setString(2, driver.getLicencePhoto());
            statement.setInt(3, driver.getUserId());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new DBException(Message.Driver.DOCUMENT_UPLOADING_FAILED);
            }
        } catch (SQLException e) {
            throw new DBException(Message.DRIVER_ALREADY_EXIST, e);
        } catch (ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
    }


    public List<Driver> getPendingVerificationDrivers() throws SQLException, ClassNotFoundException {
        List<Driver> pendingDrivers = new ArrayList<>();
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PENDING_VERIFICATION_DRIVERS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Driver driver = new Driver();
                driver.setDriverId(resultSet.getInt("driver_id"));
                User user = userDAO.getUserDetails(resultSet.getInt("user_id"));
                driver.setUserId(resultSet.getInt("user_id"));
                driver.setEmailId(user.getEmailId());
                driver.setFirstName(user.getFirstName());
                driver.setLastName(user.getLastName());
                driver.setRole(user.getRole());
                driver.setDisplayId(user.getDisplayId());
                driver.setLicenceNumber(resultSet.getString("licence_number"));
                driver.setLicencePhoto(resultSet.getString("licence_photo"));
                driver.setDocumentVerified(resultSet.getBoolean("is_document_verified"));
                driver.setVerificationStatus(resultSet.getString("verification_status"));
                driver.setComment(resultSet.getString("comment"));
                pendingDrivers.add(driver);
            }
        }
        return pendingDrivers;
    }

    public boolean isDriverExist(Integer driverId) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_DRIVER_EXISTENCE)) {
            preparedStatement.setInt(1, driverId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
    }

    public void verifyDriver(Integer driverId, boolean isVerified, String rejectionMessage) throws DBException {
        try (Connection connection = DBConnection.INSTANCE.getConnection()) {
            try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_DRIVER_DOCUMENTS)) {
                checkStmt.setInt(1, driverId);
                try (ResultSet resultSet = checkStmt.executeQuery()) {
                    if (resultSet.next()) {
                        String licenceNumber = resultSet.getString("licence_number");
                        if (licenceNumber == null || licenceNumber.trim().isEmpty()) {
                            throw new DBException(Message.Driver.DOCUMENT_NOT_UPLOADED);
                        }
                    } else {
                        throw new DBException(Message.Driver.DRIVER_NOT_EXIST);
                    }
                }
            }
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DRIVER_VERIFICATION_STATUS)) {
                preparedStatement.setString(1, isVerified ? "ACCEPTED" : "REJECTED");
                preparedStatement.setString(2, isVerified ? null : rejectionMessage);
                preparedStatement.setInt(3, driverId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
    }

    public List<Driver> fetchAllDrivers() throws DBException {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DRIVERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Driver driver = new Driver();
                driver.setDriverId(resultSet.getInt("driver_id"));
                driver.setUserId(resultSet.getInt("user_id"));
                driver.setLicenceNumber(resultSet.getString("licence_number"));
                driver.setDocumentVerified(resultSet.getBoolean("is_document_verified"));
                driver.setAvailable(resultSet.getBoolean("is_available"));
                driver.setVerificationStatus(resultSet.getString("verification_status"));
                driver.setComment(resultSet.getString("comment"));
                User userDrive = userDAO.getUserDetails(driver.getUserId());
                if (userDrive != null) {
                    driver.setFirstName(userDrive.getFirstName());
                    driver.setLastName(userDrive.getLastName());
                    driver.setEmailId(userDrive.getEmailId());
                }
                drivers.add(driver);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
        return drivers;
    }


    public List<Driver> fetchPendingVerificationDrivers() throws DBException {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_UNVERIFIED_DRIVER);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Driver driver = new Driver();
                driver.setDriverId(resultSet.getInt("driver_id"));
                driver.setFirstName(resultSet.getString("first_name"));
                driver.setLastName(resultSet.getString("last_name"));
                driver.setEmailId(resultSet.getString("email_id"));
                driver.setAvailable(resultSet.getBoolean("available"));
                driver.setDocumentVerified(resultSet.getBoolean("document_verified"));
                driver.setActive(resultSet.getBoolean("is_active"));
                drivers.add(driver);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
        return drivers;
    }

    public Integer getDriverIdFromUserId(int userId) throws DBException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_DRIVERID_FROM_USERID)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("driver_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
        return null;
    }

    public boolean isDriverDocumentVerified(int driverId) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DOCUMENT_VERIFIED)) {

            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_document_verified");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new ApplicationException("Error checking driver document verification.", e);
        }
        return false;
    }

    public boolean isDriverDocumentUploaded(int driverId) throws DBException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CHECK_DRIVER_DOCUMENTS)) {
            stmt.setInt(1, driverId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String licenceNumber = resultSet.getString("licence_number");
                    return licenceNumber != null && !licenceNumber.trim().isEmpty();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.DATABASE_ERROR, e);
        }
        return false;
    }
}