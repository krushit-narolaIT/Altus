package com.krushit.dao;

import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Feedback;

public interface IFeedbackDAO {
    void saveFeedback(Feedback feedback) throws DBException;
    int getRecipientUserIdByRideId(int rideId, Role userRole) throws DBException;
    boolean isFeedbackGiven(int fromUserId, int toUserId, int rideId) throws DBException;
}
