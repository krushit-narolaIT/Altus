package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.utils.DBConnection;

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
    private final String GET_ROLE = "SELECT role FROM roles WHERE role_id = ?";
    private final String CHECK_USER_EXISTENCE = "SELECT COUNT(*) FROM users WHERE email_id = ?";
    private final String INSERT_DRIVER_ENTRY = "INSERT INTO drivers (user_id) VALUES (?)";
    private final String GET_ALL_USERS = "SELECT * FROM users WHERE role_id = 2";

    public void registerUser(User user) throws ApplicationException, SQLException, ClassNotFoundException {
        if (isUserExist(user.getEmailId())) {
            throw new ApplicationException(Message.USER_ALREADY_EXIST);
        }
        try (Connection connection = DBConnection.INSTANCE.getConnection()) {
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
                throw new DBException(Message.DATABASE_ERROR, new SQLException("User ID generation failed"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.GENERIC_ERROR, e);
        }
    }

    public User userLogin(String emailId, String password) {
        System.out.println("In dao");
        User user = null;
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(USER_LOGIN)) {
            statement.setString(1, emailId);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setRole(Role.getRole(resultSet.getInt("role_id")));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPhoneNo(resultSet.getString("phone_no"));
                user.setEmailId(resultSet.getString("email_id"));
                user.setActive(resultSet.getBoolean("is_active"));
                user.setDisplayId(resultSet.getString("display_id"));
                user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                user.setCreatedBy(resultSet.getString("created_by"));
                user.setUpdatedBy(resultSet.getString("updated_by"));
            }
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean isUserExist(String emailID) throws SQLException, ClassNotFoundException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(CHECK_USER_EXISTENCE)) {
            checkStmt.setString(1, emailID);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        }
        return false;
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

    public User getUserDetails(int userId) {
        User user = null;
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_DETAIL)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
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
                user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
                user.setCreatedBy(resultSet.getString("created_by"));
                user.setUpdatedBy(resultSet.getString("updated_by"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> fetchAllCustomers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setEmailId(resultSet.getString("email_id"));
                user.setActive(resultSet.getBoolean("is_active"));

                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}