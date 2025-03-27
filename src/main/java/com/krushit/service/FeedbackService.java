package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dao.FeedbackDAOImpl;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.dto.FeedbackDTO;
import com.krushit.model.User;

public class FeedbackService {
    private final FeedbackDAOImpl feedbackDAO = new FeedbackDAOImpl();
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final IUserDAO userDAO = new UserDAOImpl();

    public void submitFeedback(int fromUserId, int toUserId, FeedbackDTO feedbackDTO) throws ApplicationException {
        int rideId = feedbackDTO.getRideId();
        if(vehicleRideService.fetchRideStatus(rideId) == null){
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_FEEDBACK_AFTER_RIDE_COMPLETED);
        }
        int rating = feedbackDTO.getRating();
        String comment = feedbackDTO.getComment();
        if (rating < 1 || rating > 5) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_RATING);
        }
        User fromUser = userDAO.getUserDetails(fromUserId);
        User toUser = userDAO.getUserDetails(toUserId);
        if (fromUser == null || toUser == null) {
            throw new ApplicationException(Message.User.INVALID_USER_ID);
        }
        if (feedbackDAO.isFeedbackGiven(fromUserId, toUserId, rideId)) {
            throw new IllegalArgumentException(Message.FeedBack.FEEDBACK_ALREADY_SUBMITTED);
        }
        feedbackDAO.saveFeedback(fromUserId, toUserId, rideId, rating, comment);
    }

    public int getToUserId(int rideId, String userRole) throws ApplicationException {
        int toUserId = feedbackDAO.findToUserIdByRide(rideId, userRole);
        if (toUserId == 0) {
            throw new ApplicationException(Message.User.INVALID_USER_ID);
        }
        return toUserId;
    }
}
