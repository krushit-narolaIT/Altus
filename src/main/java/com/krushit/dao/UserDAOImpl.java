package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.common.enums.Role;
import com.krushit.model.User;
import com.krushit.common.config.DBConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements IUserDAO {
    private final String INSERT_USER_DATA = "INSERT INTO users (role_id , first_name, last_name, phone_no, email_id, password, display_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String CREATE_DISPLAY_ID = "UPDATE users SET display_id = ? WHERE user_id = ?";
    private final String USER_LOGIN = "SELECT * FROM users WHERE email_id = ? AND password = ?";
    private final String GET_USER_DETAIL = "SELECT * FROM users WHERE user_id = ?";
    private final String CHECK_USER_EXISTENCE = "SELECT 1 FROM users WHERE email_id = ? OR phone_no = ?";
    private final String INSERT_DRIVER_ENTRY = "INSERT INTO drivers (user_id) VALUES (?)";
    private final String GET_ALL_USERS = "SELECT * FROM users WHERE role_id = 2";
    private final String CHECK_USER_CREDENTIALS = "SELECT 1 FROM users WHERE email_id = ? AND password = ?";
    private final String GET_DISPLAY_ID_FROM_USER_ID = "SELECT display_id FROM users WHERE user_id = ?";
    private final String GET_FULL_NAME_FROM_USER_ID = "SELECT first_name, last_name FROM users WHERE user_id = ?";
    private final String GET_PHONE_NO = "SELECT display_id FROM users WHERE user_id = ?";

    public void registerUser(User user) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection()) {
            connection.setAutoCommit(false);
            int userId = -1;
            try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_USER_DATA, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStmt.setInt(1, user.getRole().getRoleId());
                insertStmt.setString(2, user.getFirstName());
                insertStmt.setString(3, user.getLastName());
                insertStmt.setString(4, user.getPhoneNo());
                insertStmt.setString(5, user.getEmailId());
                insertStmt.setString(6, user.getPassword());
                insertStmt.setString(7, String.valueOf(System.currentTimeMillis() % 10000));
                if (insertStmt.executeUpdate() > 0) {
                    try (ResultSet autoGeneratedKey = insertStmt.getGeneratedKeys()) {
                        if (autoGeneratedKey.next()) {
                            userId = autoGeneratedKey.getInt(1);
                        }
                    }
                }
            }
            if (userId > 0) {
                String displayId = user.getRole().getRoleName().equalsIgnoreCase("Driver")
                        ? generateDriverDisplayId(userId)
                        : generateCustomerDisplayId(userId);
                try (PreparedStatement updateStmt = connection.prepareStatement(CREATE_DISPLAY_ID)) {
                    updateStmt.setString(1, displayId);
                    updateStmt.setInt(2, userId);
                    updateStmt.executeUpdate();
                }
                if (user.getRole().getRoleName().equalsIgnoreCase("Driver")) {
                    try (PreparedStatement driverStmt = connection.prepareStatement(INSERT_DRIVER_ENTRY)) {
                        driverStmt.setInt(1, userId);
                        driverStmt.executeUpdate();
                    }
                }
                connection.commit();
            } else {
                connection.rollback();
                throw new DBException(Message.DATABASE_ERROR, new SQLException("failed to generate user id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_REGISTERING_USER, e);
        }
    }

    public User userLogin(String emailId, String password) throws DBException {
        User user = null;
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(USER_LOGIN)) {
            statement.setString(1, emailId);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User.UserBuilder()
                        .setUserId(resultSet.getInt("user_id"))
                        .setRole(Role.getRole(resultSet.getInt("role_id")))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setPhoneNo(resultSet.getString("phone_no"))
                        .setEmailId(resultSet.getString("email_id"))
                        .setActive(resultSet.getBoolean("is_active"))
                        .setDisplayId(resultSet.getString("display_id"))
                        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .setCreatedBy(resultSet.getString("created_by"))
                        .setUpdatedBy(resultSet.getString("updated_by"))
                        .build();
            }
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_USER_LOGIN, e);
        }
        return user;
    }

    public boolean isValidUser(String emailID, String password) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_USER_CREDENTIALS)) {
            statement.setString(1, emailID);
            statement.setString(2, password);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_VALIDATING_USER, e);
        }
    }

    public boolean isUserExist(String emailID, String phoneNo) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_USER_EXISTENCE)) {
            statement.setString(1, emailID);
            statement.setString(2, phoneNo);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_CHECKING_USER_EXISTENCE, e);
        }
    }

    private String generateCustomerDisplayId(int userId) {
        String timestampPart = String.valueOf(System.currentTimeMillis() % 1000);
        String userIdPart = String.format("%04d", userId % 10000);
        return "US" + userIdPart + "R" + timestampPart;
    }

    private String generateDriverDisplayId(int userId) {
        String timestampPart = String.valueOf(System.currentTimeMillis() % 1000);
        String userIdPart = String.format("%04d", userId % 10000);
        return "DR" + userIdPart + "V" + timestampPart;
    }

    public User getUserDetails(int userId) throws DBException {
        User user = null;
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_DETAIL)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User.UserBuilder()
                        .setUserId(resultSet.getInt("user_id"))
                        .setRole(Role.getRole(resultSet.getInt("role_id")))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setPhoneNo(resultSet.getString("phone_no"))
                        .setEmailId(resultSet.getString("email_id"))
                        .setDisplayId(resultSet.getString("display_id"))
                        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .setCreatedBy(resultSet.getString("created_by"))
                        .setUpdatedBy(resultSet.getString("updated_by"))
                        .build();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GET_USER_DETAILS, e);
        }
        return user;
    }

    public List<User> fetchAllCustomers() throws DBException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User.UserBuilder()
                        .setUserId(resultSet.getInt("user_id"))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setEmailId(resultSet.getString("email_id"))
                        .setActive(resultSet.getBoolean("is_active"))
                        .build();
                users.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_ALL_CUSTOMERS, e);
        }
        return users;
    }

    public String getUserDisplayIdById(int userId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_DISPLAY_ID_FROM_USER_ID)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("display_id");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_DISPLAY_ID, e);
        }
        return null;
    }

    public String getUserFullNameById(int userId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_FULL_NAME_FROM_USER_ID)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                return firstName + " " + lastName;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_USER_FULL_NAME, e);
        }
        return null;
    }
}