package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.enums.RoleType;
import com.krushit.common.exception.ApplicationException;
import com.krushit.dao.FeedbackDAOImpl;
import com.krushit.dao.IFeedbackDAO;
import com.krushit.dto.FeedbackDTO;
import com.krushit.entity.Feedback;

public class FeedbackService {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final UserService userService = new UserService();
    private final IFeedbackDAO feedbackDAO = new FeedbackDAOImpl();

    public void submitFeedback(int fromUserId, FeedbackDTO feedbackDTO, int rideId) throws ApplicationException {
        //int toUserId = getToUserId(rideId, userService.getUserDetails(fromUserId).get().getRole());
        int toUserId = 0;
        if (vehicleRideService.getRideStatus(rideId) == null) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_FEEDBACK_AFTER_RIDE_COMPLETED);
        }
        int rating = feedbackDTO.getRating();
        String comment = feedbackDTO.getComment();
        if (rating < 1 || rating > 5) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_RATING);
        }
        userService.getUserDetails(fromUserId)
                .orElseThrow(() -> new ApplicationException(Message.User.USER_NOT_FOUND));
        userService.getUserDetails(toUserId)
                .orElseThrow(() -> new ApplicationException(Message.User.USER_NOT_FOUND));
        if (feedbackDAO.isFeedbackGiven(fromUserId, toUserId, rideId)) {
            throw new ApplicationException(Message.FeedBack.FEEDBACK_ALREADY_SUBMITTED);
        }
        Feedback feedback = new Feedback.FeedbackBuilder()
                //.setRideId(rideId)
               // .setFromUserId(fromUserId)
                //.setToUserId(toUserId)
                .setComment(comment)
                .setRating(rating)
                .build();
        System.out.println("Feed Back :: " + feedback);
        feedbackDAO.saveFeedback(feedback);
    }


    public int getToUserId(int rideId, RoleType userRoleType) throws ApplicationException {
        int toUserId = feedbackDAO.getRecipientUserIdByRideId(rideId, userRoleType);
        if (toUserId == 0) {
            throw new ApplicationException(Message.User.INVALID_USER_ID);
        }
        return toUserId;
    }
}
