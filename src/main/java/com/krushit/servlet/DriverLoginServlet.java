package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.entity.User;
import com.krushit.exception.ValidationException;
import com.krushit.model.ApiResponse;
import com.krushit.service.DriverService;
import com.krushit.utils.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class DriverLoginServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();
    //.registerModule(new JavaTimeModule());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);

        User loginUser = objectMapper.readValue(request.getReader(), User.class);
        String email = loginUser.getEmailId();
        String password = loginUser.getPassword();

        try {
            Validation.validateLoginCredentials(email, password);

            User authenticatedDriver = driverService.driverLogin(email, password);
            System.out.println("Here");
            if (authenticatedDriver != null) {
                System.out.println("Authenticated User :: " + authenticatedDriver);
                HttpSession session = request.getSession(true);
                session.setAttribute("user", authenticatedDriver);

                sendResponse(response, HttpServletResponse.SC_OK, Message.LOGIN_SUCCESSFUL, authenticatedDriver);
            } else {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, Message.INVALID_EMAIL_AND_PASS, null);
            }
        } catch (ValidationException e) {
            sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
