package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponseDTO;
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

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/giveFeedback")
public class GiveUserFeedbackController extends HttpServlet {
    private final FeedbackService feedbackService = new FeedbackService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            User user = SessionUtils.validateSession(request);
            int rideId = Integer.parseInt(request.getParameter("rideId"));
            FeedbackDTO feedbackDTO = ObjectMapperUtils.toObject(request.getReader(), FeedbackDTO.class);
            int toUserId = feedbackService.getToUserId(rideId, user.getRole());
            feedbackService.submitFeedback(user.getUserId(), toUserId, feedbackDTO, rideId);
            createResponse(response, Message.FeedBack.FEEDBACK_SUBMITTED, null, HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
