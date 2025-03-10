package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.entity.User;
import com.krushit.exception.GenericException;
import com.krushit.model.ApiResponse;
import com.krushit.service.DriverService;
import com.krushit.exception.DBException;
import com.krushit.utils.Validation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class DriverSignUpServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        response.setCharacterEncoding("UTF-8");

        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                throw new GenericException(Message.INVALID_CONTENT_TYPE);
            }

            User user = objectMapper.readValue(request.getReader(), User.class);
            Validation.validateUser(user);

            driverService.registerDriver(user);

            createResponse(response, Message.DRIVER_REGISTERED_SUCCESSFULLY, user.getEmailId());
        } catch (GenericException | DBException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            createResponse(response, "Database Error: " + e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            createResponse(response, Message.INTERNAL_SERVER_ERROR + e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private void createResponse(HttpServletResponse response, String message, Object data) throws IOException {
        int statusCode;
        if (Message.DRIVER_REGISTERED_SUCCESSFULLY.equals(message)) {
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
        ApiResponse apiResponse = null;
        if(data == null){
            apiResponse = new ApiResponse(message);
        } else {
            apiResponse = new ApiResponse(message, data);
        }
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
