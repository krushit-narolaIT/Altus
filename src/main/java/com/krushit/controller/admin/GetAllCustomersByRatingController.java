package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.dto.UserDTO;
import com.krushit.model.Driver;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import com.krushit.service.UserService;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
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
            ApplicationUtils.validateJsonRequest(request.getContentType());
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            String ratingParam = request.getParameter("rating");
            String reviewParam = request.getParameter("reviews");
            if (ratingParam == null || reviewParam == null) {
                createResponse(response, Message.FeedBack.MISSING_RATING_REVIEW_COUNT_PARAMS, null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            int ratingThreshold = Integer.parseInt(ratingParam);
            int reviewThreshold = Integer.parseInt(reviewParam);
            List<User> users = userService.getUsersWithLessRatingAndReviews(ratingThreshold, reviewThreshold);
            if (users.isEmpty()) {
                createResponse(response, Message.Driver.NO_DRIVERS_FOUND, null, HttpServletResponse.SC_NO_CONTENT);
            } else {
                createResponse(response, Message.Driver.SUCCESSFULLY_RETRIEVED_DRIVERS, users, HttpServletResponse.SC_OK);
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