package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.model.Driver;
import com.krushit.dto.ApiResponse;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class DriverVerificationController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.INVALID_CONTENT_TYPE);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            System.out.println("User :: " + user.getRole());
            if(user.getRole().getRoleId() != 1){
                createResponse(response, Message.UNAUTHORIZED, null, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            System.out.println("In doGet");

            List<Driver> pendingDrivers = driverService.getPendingVerificationDrivers();
            System.out.println("Pending Drivers :: " + pendingDrivers);

            if (pendingDrivers.isEmpty()) {
                createResponse(response, Message.Driver.NO_PENDING_VERIFICATION, null, HttpServletResponse.SC_OK);
            } else {
                createResponse(response, Message.Driver.SUCCESSFULLY_RETRIEVE_DRIVERS, pendingDrivers, HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.Driver.FAILED_TO_INSERT_DRIVER_DETAIL, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = null;
        if(data == null){
            apiResponse = new ApiResponse(message);
        } else {
            apiResponse = new ApiResponse(message, data);
        }
        System.out.println("API Response :: " + apiResponse);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private void createResponse(HttpServletResponse response, String message, Object data) throws IOException {
        createResponse(response, message, data, HttpServletResponse.SC_CREATED);
    }
}
