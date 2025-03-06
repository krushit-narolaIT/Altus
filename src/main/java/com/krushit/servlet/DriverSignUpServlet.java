package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.entity.User;
import com.krushit.model.ApiResponse;
import com.krushit.service.DriverServiceImpl;
import com.krushit.service.CustomerServiceImpl;
import com.krushit.utils.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DriverSignUpServlet extends HttpServlet {
    private final DriverServiceImpl driverService = new DriverServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        System.out.println("in doPost");
        if (!"application/json".equals(request.getContentType())) {
            System.out.println("Here");
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.INVALID_CONTENT_TYPE, null);
            return;
        }

        try {
            User user = objectMapper.readValue(request.getReader(), User.class);
            System.out.println("Received User: " + user);

            if (!Validation.isValidPassword(user.getPassword())) {
                sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.PLEASE_ENTER_VALID_PASSWORD, null);
                return;
            }

            if (!Validation.isValidPhoneNumber(user.getPhoneNo())) {
                sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.PLEASE_ENTER_VALID_PHONE_NO, null);
                return;
            }

            if (!Validation.isValidEmail(user.getEmailId())) {
                sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.PLEASE_ENTER_VALID_EMAIL, null);
                return;
            }

            String message = driverService.registerDriver(user);
            System.out.println("Message :: " + message);

            if (Message.DRIVER_REGISTERED_SUCCESSFULLY.equals(message)) {
                sendResponse(response, HttpServletResponse.SC_CREATED, Message.DRIVER_REGISTERED_SUCCESSFULLY, user.getEmailId());
            } else if (Message.DRIVER_ALREADY_EXIST.equals(message)) {
                sendResponse(response, HttpServletResponse.SC_CONFLICT, Message.DRIVER_ALREADY_EXIST, null);
            } else {
                sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.INTERNAL_SERVER_ERROR, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.INVALID_CONTENT_TYPE, null);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
