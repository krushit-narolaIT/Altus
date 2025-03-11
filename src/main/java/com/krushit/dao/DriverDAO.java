package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.entity.Driver;
import com.krushit.entity.Role;
import com.krushit.entity.User;
import com.krushit.exception.DBException;
import com.krushit.exception.GenericException;
import com.krushit.utils.DBConnection;
import com.krushit.utils.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    private UserDAO userDAO = new UserDAO();

    private final String INSERT_DRIVER_DATA = "INSERT INTO users (role_id, first_name, last_name, phone_no, email_id, password, display_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String CREATE_DISPLAY_ID = "UPDATE users SET display_id = ? WHERE user_id = ?";
    private final String DRIVER_LOGIN = "SELECT * FROM users WHERE email_id = ? AND password = ?";
    private final String GET_ROLE = "SELECT role FROM roles WHERE role_id = ?";
    private final String CHECK_DRIVER_EXISTENCE = "SELECT COUNT(*) FROM users WHERE email_id = ?";
    private final String INSERT_DRIVER_DETAILS = "INSERT INTO Drivers (user_id, licence_number, licence_photo) VALUES (?, ?, ?)";
    private final String GET_PENDING_VERIFICATION_DRIVERS = "SELECT * FROM drivers WHERE varification_status = FALSE";

    public void registerDriver(User driver) throws SQLException, GenericException, DBException {
        try (Connection connection = DBConnection.getConnection()) {
            if (isDriverExist(driver.getEmailId()))
                throw new GenericException(Message.DRIVER_ALREADY_EXIST);

            connection.setAutoCommit(false);
            int userId = -1;

            try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_DRIVER_DATA, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, driver.getRole().getRoleId());
                insertStmt.setString(2, driver.getFirstName());
                insertStmt.setString(3, driver.getLastName());
                insertStmt.setString(4, driver.getPhoneNo());
                insertStmt.setString(5, driver.getEmailId());
                insertStmt.setString(6, driver.getPassword());
                insertStmt.setString(7, String.valueOf(System.currentTimeMillis() % 10000));

                int count = insertStmt.executeUpdate();
                if (count > 0) {
                    try (ResultSet autoGeneratedKey = insertStmt.getGeneratedKeys()) {
                        if (autoGeneratedKey.next()) {
                            userId = autoGeneratedKey.getInt(1);
                        }
                    }
                }
            }

            if (userId > 0) {
                String displayId = generateDisplayId(userId);
                try (PreparedStatement updateStmt = connection.prepareStatement(CREATE_DISPLAY_ID)) {
                    updateStmt.setString(1, displayId);
                    updateStmt.setInt(2, userId);
                    updateStmt.executeUpdate();
                }
                connection.commit();
            } else {
                connection.rollback();
                throw new DBException(Message.DATABASE_ERROR);
            }
        } catch (SQLException e) {
            throw new DBException(Message.DRIVER_ALREADY_EXIST);
        }
    }

    public User driverLogin(String emailId, String password) {
        User user = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DRIVER_LOGIN)) {

            statement.setString(1, emailId);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setUserId(resultSet.getInt("user_id"));

                    int roleId = resultSet.getInt("role_id");
                    System.out.println("Role Id :: " + roleId);

                    Role role = null;
                    for (Role r : Role.values()) {
                        if (r.getRoleId() == roleId) {
                            role = r;
                            break;
                        }
                    }
                    user.setRole(role);

                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPhoneNo(resultSet.getString("phone_no"));
                    user.setEmailId(resultSet.getString("email_id"));
                    user.setDisplayId(resultSet.getString("display_id"));
//                    user.setCreatedAt(DateUtils.toLocalDateTime(resultSet.getTimestamp("created_at")));
//                    user.setUpdatedAt(DateUtils.toLocalDateTime(resultSet.getTimestamp("updated_at")));
                    user.setCreatedBy(resultSet.getString("created_by"));
                    user.setUpdatedBy(resultSet.getString("updated_by"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getRole(int role_id) {
        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(GET_ROLE)) {
            statement.setInt(1, role_id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isDriverExist(String emailID) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(CHECK_DRIVER_EXISTENCE)) {
            checkStmt.setString(1, emailID);
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        }
        return false;
    }

    private String generateDisplayId(int userId) {
        String timestampPart = String.valueOf(System.currentTimeMillis() % 1000);
        String userIdPart = String.format("%04d", userId % 10000);
        return "DR" + userIdPart + "V" + timestampPart;
    }

    public void insertDriverDetails(Driver driver) throws SQLException, DBException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_DRIVER_DETAILS)) {

            statement.setInt(1, driver.getUserId());
            statement.setString(2, driver.getLicenceNumber());
            statement.setString(3, driver.getLicencePhoto());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted == 0) {
                throw new DBException(Message.Driver.FAILED_TO_INSERT_DRIVER_DETAIL);
            }
        } catch (DBException e){
            throw new DBException(Message.DRIVER_ALREADY_EXIST);
        }
    }

    public List<Driver> getPendingVerificationDrivers() throws SQLException {
        System.out.println("In dao");
        List<Driver> pendingDrivers = new ArrayList<>();
        System.out.println("Before Pending Driver :: " + pendingDrivers);

        try (Connection connection = DBConnection.getConnection();
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
                driver.setVerificationStatus(resultSet.getString("varification_status"));
                driver.setComment(resultSet.getString("comment"));

                System.out.println("Driver :: " + driver);
                pendingDrivers.add(driver);
            }
        }
        System.out.println("After Pending Driver :: " + pendingDrivers);
        return pendingDrivers;
    }


}
