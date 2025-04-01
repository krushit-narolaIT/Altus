package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedbackDAOImpl implements IFeedbackDAO {
    private static final String IS_FEEDBACK_GIVEN = "SELECT COUNT(*) FROM feedback WHERE from_user_id = ? AND to_user_id = ? AND ride_id = ?";
    private static final String SAVE_FEEDBACK = "INSERT INTO feedback (from_user_id, to_user_id, ride_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
    private final IUserDAO userDAO = new UserDAOImpl();

    public void saveFeedback(int fromUserId, int toUserId, int rideId, int rating, String comment) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_FEEDBACK)) {
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
            throw new DBException(Message.FeedBack.ERROR_WHILE_ADDING_FEEDBACK, e);
        }
    }

    public int findToUserIdByRide(int rideId, Role role) throws DBException {
        String query = "SELECT customer_id FROM rides WHERE ride_id = ?";;
        if (Role.ROLE_CUSTOMER == role) {
            query = "SELECT driver_id FROM rides WHERE ride_id = ?";
        }
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, rideId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.FeedBack.ERROR_WHILE_FETCHING_TO_FEEDBACK, e);
        }
        return 0;
    }

    public boolean isFeedbackGiven(int fromUserId, int toUserId, int rideId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(IS_FEEDBACK_GIVEN)) {
            pstmt.setInt(1, fromUserId);
            pstmt.setInt(2, toUserId);
            pstmt.setInt(3, rideId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.FeedBack.ERROR_WHILE_CHECKING_IS_FEEDBACK_GIVEN, e);
        }
        return false;
    }
}
