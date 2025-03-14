package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.exception.ApplicationException;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import com.krushit.utils.SignupValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSignUpController extends HttpServlet {
    private final CustomerService userService = new CustomerService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        response.setCharacterEncoding(Message.UTF_8);
        System.out.println("in doPost");
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                throw new ApplicationException(Message.INVALID_CONTENT_TYPE);
            }
            String path = request.getServletPath();
            User user = objectMapper.readValue(request.getReader(), User.class);
            SignupValidator.validateUser(user);
            if(path.equalsIgnoreCase(Message.Customer.CUSTOMER_PATH)){
                user.setRole(Role.ROLE_CUSTOMER);
            } else {
                user.setRole(Role.ROLE_DRIVER);
            }
            userService.registerUser(user);
            createResponse(response, Message.USER_REGISTERED_SUCCESSFULLY, user.getEmailId());
        } catch (ApplicationException e) {
            e.printStackTrace();
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.INTERNAL_SERVER_ERROR + e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private void createResponse(HttpServletResponse response, String message, Object data) throws IOException {
        int statusCode;
        if (Message.USER_REGISTERED_SUCCESSFULLY.equals(message)) {
            statusCode = HttpServletResponse.SC_CREATED;
        } else if (Message.DRIVER_ALREADY_EXIST.equals(message)) {
            statusCode = HttpServletResponse.SC_CONFLICT;
        } else if (Message.INTERNAL_SERVER_ERROR.equals(message)) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        } else {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }
        createResponse(response, message, data, statusCode);
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
