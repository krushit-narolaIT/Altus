package com.krushit.dao;

import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackDAOImpl implements IFeedbackDAO {
    private final IUserDAO userDAO = new UserDAOImpl();

    public void saveFeedback(int fromUserId, int toUserId, int rideId, int rating, String comment) throws DBException {
        String query = "INSERT INTO feedback (from_user_id, to_user_id, ride_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setInt(1, fromUserId);
            statement.setInt(2, toUserId);
            statement.setInt(3, rideId);
            statement.setInt(4, rating);
            statement.setString(5, comment);
            statement.executeUpdate();
            userDAO.updateUserRating(toUserId, rating, connection);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException("Error saving feedback", e);
        }
    }

    public int findToUserIdByRide(int rideId, String userRole) throws DBException {
        String query;
        if (Role.ROLE_CUSTOMER.getRoleName().equals(userRole)) {
            query = "SELECT driver_id FROM rides WHERE ride_id = ?";
        } else {
            query = "SELECT customer_id FROM rides WHERE ride_id = ?";
        }
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, rideId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException("Error fetching toUserId from ride", e);
        }
        return 0;
    }

    public boolean isFeedbackGiven(int fromUserId, int toUserId, int rideId) throws DBException {
        String sql = "SELECT COUNT(*) FROM feedback WHERE from_user_id = ? AND to_user_id = ? AND ride_id = ?";
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, fromUserId);
            pstmt.setInt(2, toUserId);
            pstmt.setInt(3, rideId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException("Error fetching toUserId from ride", e);
        }
        return false;
    }
}
