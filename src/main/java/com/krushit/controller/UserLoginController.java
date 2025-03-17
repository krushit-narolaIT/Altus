package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krushit.common.Message;
import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.exception.ValidationException;
import com.krushit.model.User;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import com.krushit.utils.LoginValidator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class UserLoginController extends HttpServlet {
    private CustomerService userService = new CustomerService();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User loginUser = objectMapper.readValue(request.getReader(), User.class);
            LoginValidator.validateLoginCredentials(loginUser);
            String email = loginUser.getEmailId();
            String password = loginUser.getPassword();
            User authenticatedUser = userService.userLogin(email, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", authenticatedUser);
            sendResponse(response, HttpServletResponse.SC_OK, Message.User.LOGIN_SUCCESSFUL, authenticatedUser);
        } catch (ValidationException e) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (DBException e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        } catch (ApplicationException e) {
            sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}