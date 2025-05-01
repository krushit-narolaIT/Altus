package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.entity.User;
import com.krushit.service.UserService;
import com.krushit.utils.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/getAllCustomersByRating")
public class GetAllCustomersByRatingController extends HttpServlet {
    private final UserService userService = new UserService();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            String rating = request.getParameter("rating");
            String total_rating = request.getParameter("reviews");
            if (rating == null || total_rating == null || rating.isEmpty() || total_rating.isEmpty()) {
                createResponse(response, Message.FeedBack.MISSING_RATING_REVIEW_COUNT_PARAMS, null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            int ratingThreshold = Integer.parseInt(rating);
            int reviewThreshold = Integer.parseInt(total_rating);
            List<UserDTO> users = userService.getUsersWithLessRatingAndReviews(ratingThreshold, reviewThreshold);
            if (users.isEmpty()) {
                createResponse(response, Message.User.NO_USERS_FOUND, null, HttpServletResponse.SC_NO_CONTENT);
            } else {
                createResponse(response, Message.User.SUCCESSFULLY_RETRIEVED_USERS, users, HttpServletResponse.SC_OK);
            }
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}