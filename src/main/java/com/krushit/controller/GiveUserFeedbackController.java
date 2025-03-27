package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.FeedbackDTO;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import com.krushit.service.FeedbackService;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/giveFeedback")
public class GiveUserFeedbackController extends HttpServlet {
    private final FeedbackService feedbackService = new FeedbackService();
    private final Mapper mapper =Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            UserDTO userDTO = SessionUtils.validateSession(request);
            User user = mapper.convertToEntityUserDTO(userDTO);
            //AuthValidator.validateUser(user, Role.ROLE_CUSTOMER.getRoleName());
            FeedbackDTO feedbackDTO = ObjectMapperUtils.toObject(request.getReader(), FeedbackDTO.class);
            int toUserId = feedbackService.getToUserId(feedbackDTO.getRideId(), user.getRole().getRoleName());
            feedbackService.submitFeedback(user.getUserId(), toUserId, feedbackDTO);
            sendResponse(response, HttpServletResponse.SC_OK, Message.FeedBack.FEEDBACK_SUBMITTED, null);
        } catch (DBException e) {
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        } catch (ApplicationException e) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponse));
    }
}
