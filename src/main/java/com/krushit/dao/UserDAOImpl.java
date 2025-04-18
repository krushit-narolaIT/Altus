package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.enums.DocumentVerificationStatus;
import com.krushit.common.enums.RoleType;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Driver;
import com.krushit.entity.Role;
import com.krushit.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements IUserDAO {
    private static final String INSERT_USER_DATA = "INSERT INTO users (role_id , first_name, last_name, phone_no, email_id, password, display_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String CREATE_DISPLAY_ID = "UPDATE users SET display_id = ? WHERE user_id = ?";
    private static final String USER_LOGIN = "SELECT * FROM users WHERE email_id = ? AND password = ?";
    private static final String GET_USER_DETAIL = "SELECT * FROM users WHERE user_id = ?";
    private static final String CHECK_USER_EXISTENCE = "SELECT 1 FROM users WHERE email_id = ? OR phone_no = ?";
    private static final String CHECK_USER_EXISTENCE_BY_ID = "SELECT 1 FROM users WHERE user_id = ?";
    private static final String INSERT_DRIVER_ENTRY = "INSERT INTO drivers (user_id) VALUES (?)";
    private static final String GET_ALL_USERS = "SELECT * FROM users WHERE role_id = 2";
    private static final String CHECK_USER_CREDENTIALS = "SELECT 1 FROM users WHERE email_id = ? AND password = ?";
    private static final String GET_DISPLAY_ID_FROM_USER_ID = "SELECT display_id FROM users WHERE user_id = ?";
    private static final String GET_FULL_NAME_FROM_USER_ID = "SELECT first_name, last_name FROM users WHERE user_id = ?";
    private static final String GET_USER_BY_EMAIL = "SELECT * FROM users WHERE email_id = ?";
    private static final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE email_id = ?";
    private static final String BLOCK_USER = "UPDATE users SET is_blocked = TRUE WHERE user_id = ?";
    private static final String IS_USER_BLOCKED = "SELECT is_blocked FROM users WHERE user_id = ?";
    private static final String UPDATE_USER_DETAILS = "UPDATE users SET first_name = ?, last_name = ?, phone_no = ?, email_id = ?, updated_by = ? WHERE user_id = ?";
    private static final String UPDATE_USER_RATING = "UPDATE users " +
            "SET total_ratings = ((total_ratings * rating_count) + ?) / (rating_count + 1), " +
            "rating_count = rating_count + 1 " +
            "WHERE user_id = ?;";
    private static final String GET_CUSTOMERS_BY_RATING_AND_REVIEW = "SELECT * FROM users " +
            "WHERE total_ratings < ? " +
            "AND rating_count > ? ";

    @Override
    public void registerUser(User user) throws DBException {
        EntityTransaction tx = null;
        EntityManager em = null;
        try {
            em = JPAConfig.getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            String displayId;
            RoleType roleType = user.getRole().getRoleType();
            if (roleType == RoleType.ROLE_DRIVER) {
                displayId = generateDriverDisplayId(user.getUserId());
            } else {
                displayId = generateCustomerDisplayId(user.getUserId());
            }
            user.setDisplayId(displayId);
            if (roleType == RoleType.ROLE_DRIVER) {
                Driver driver = new Driver.DriverBuilder().setUser(user).setVerificationStatus(DocumentVerificationStatus.INCOMPLETE).build();
                em.persist(driver);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DBException(Message.User.ERROR_WHILE_REGISTERING_USER, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public User getUser(String emailId, String password) throws DBException {
        User user = null;
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(USER_LOGIN)) {
            statement.setString(1, emailId);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User.UserBuilder()
                        .setUserId(resultSet.getInt("user_id"))
                        .setRole(new Role(resultSet.getInt("role_id"), null))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setPhoneNo(resultSet.getString("phone_no"))
                        .setEmailId(resultSet.getString("email_id"))
                        .setActive(resultSet.getBoolean("is_active"))
                        .setDisplayId(resultSet.getString("display_id"))
                        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .build();
            }
            resultSet.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_USER_LOGIN, e);
        }
        return user;
    }

    @Override
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

    @Override
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

    @Override
    public boolean isUserExist(int userId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_USER_EXISTENCE_BY_ID)) {
            statement.setInt(1, userId);
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

    @Override
    public Optional<User> getUser(int userId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_DETAIL)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User.UserBuilder()
                        .setUserId(resultSet.getInt("user_id"))
                        .setRole(new Role(resultSet.getInt("role_id"), null))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setPhoneNo(resultSet.getString("phone_no"))
                        .setEmailId(resultSet.getString("email_id"))
                        .setDisplayId(resultSet.getString("display_id"))
                        .setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime())
                        .build());
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GET_USER_DETAILS, e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllCustomers() throws DBException {
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

    @Override
    public String getUserDisplayId(int userId) throws DBException {
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

    @Override
    public String getUserFullName(int userId) throws DBException {
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

    @Override
    public void updateUser(User user) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_USER_DETAILS)) {
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getPhoneNo());
            stmt.setString(4, user.getEmailId());
            stmt.setString(5, RoleType.ROLE_CUSTOMER.getRoleName());
            stmt.setInt(6, user.getUserId());
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_UPDATING_USER_DETAILS, e);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User.UserBuilder()
                        .setUserId(rs.getInt("user_id"))
                        .setEmailId(rs.getString("email_id"))
                        .setPassword(rs.getString("password"))
                        .build();
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GET_USER_DETAILS, e);
        }
    }

    @Override
    public void updatePassword(String email, String newPassword) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PASSWORD)) {
            statement.setString(1, newPassword);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_UPDATING_PASSWORD, e);
        }
    }

    @Override
    public void updateUserRating(int userId, int rating, Connection connection) throws DBException {
        try (PreparedStatement prepareStatement = connection.prepareStatement(UPDATE_USER_RATING)) {
            prepareStatement.setInt(1, rating);
            prepareStatement.setInt(2, userId);
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(Message.User.ERROR_WHILE_UPDATE_DRIVER_RATING, e);
        }
    }

    @Override
    public void updateUserRating(int userId, int newRating, EntityManager em) throws DBException {
        try {
            Query query = em.createQuery(
                    "UPDATE User u SET u.totalRatings = ((u.totalRatings * u.ratingCount) + :rating) / (u.ratingCount + 1), " +
                            "u.ratingCount = u.ratingCount + 1 " +
                            "WHERE u.userId = :userId"
            ).setParameter("rating", newRating).setParameter("userId", userId);
            query.executeUpdate();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_UPDATE_DRIVER_RATING, e);
        }
    }

    @Override
    public void blockUser(int userId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BLOCK_USER)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_BLOCKING_USER, e);
        }
    }

    @Override
    public boolean isUserBlocked(int userId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(IS_USER_BLOCKED)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("is_blocked");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_CHECK_IS_USER_BLOCKED, e);
        }
        return false;
    }

    public List<User> getUsersByLowRatingAndReviewCount(int ratingThreshold, int reviewCountThreshold) throws DBException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_CUSTOMERS_BY_RATING_AND_REVIEW)) {
            ps.setInt(1, ratingThreshold);
            ps.setInt(2, reviewCountThreshold);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User.UserBuilder()
                        .setUserId(rs.getInt("user_id"))
                        .setRole(new Role(rs.getInt("role_id"), null))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setPhoneNo(rs.getString("phone_no"))
                        .setEmailId(rs.getString("email_id"))
                        .setPassword(rs.getString("password"))
                        .setDisplayId(rs.getString("display_id"))
                        .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .setTotalRatings(rs.getInt("total_ratings"))
                        .setRatingCount(rs.getInt("rating_count"))
                        .build();
                users.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_CUSTOMERS_BY_RATING, e);
        }
        return users;
    }

    @Override
    public List<User> getUsersByPagination(int offset, int limit) throws DBException{
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users LIMIT ? OFFSET ?";
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User.UserBuilder()
                .setUserId(rs.getInt("user_id"))
                        .setUserId(rs.getInt("user_id"))
                        .setRole(new Role(rs.getInt("role_id"), null))
                        .setFirstName(rs.getString("first_name"))
                        .setLastName(rs.getString("last_name"))
                        .setPhoneNo(rs.getString("phone_no"))
                        .setEmailId(rs.getString("email_id"))
                        .setDisplayId(rs.getString("display_id"))
                        .setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                        .setTotalRatings(rs.getInt("total_ratings"))
                        .setRatingCount(rs.getInt("rating_count"))
                .build();
                userList.add(user);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_CUSTOMERS_BY_RATING, e);
        }
        return userList;
    }

}