package com.krushit.dao;

import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;

public interface IFeedbackDAO {
    void saveFeedback(int fromUserId, int toUserId, int rideId, int rating, String comment) throws DBException;
    int findToUserIdByRide(int rideId, Role userRole) throws DBException;
}
