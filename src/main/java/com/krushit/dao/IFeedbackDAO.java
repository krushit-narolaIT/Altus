package com.krushit.dao;

import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;
import com.krushit.model.Feedback;

public interface IFeedbackDAO {
    void saveFeedback(Feedback feedback) throws DBException;
    int findToUserIdByRide(int rideId, Role userRole) throws DBException;
}
