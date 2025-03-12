package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.dto.ApiResponse;
import com.krushit.model.Driver;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class GetAllDriversController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            System.out.println("User :: " + user.getRole());
            if (user.getRole().getRoleId() != 1) {
                createResponse(response, Message.UNAUTHORIZED, null, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            System.out.println("Fetching all drivers...");
            List<Driver> drivers = driverService.getAllDrivers();
            System.out.println("Drivers Retrieved :: " + drivers);

            if (drivers.isEmpty()) {
                createResponse(response, Message.Driver.NO_DRIVERS_FOUND, null, HttpServletResponse.SC_OK);
            } else {
                createResponse(response, Message.Driver.SUCCESSFULLY_RETRIEVED_DRIVERS, drivers, HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.Driver.FAILED_TO_RETRIEVE_DRIVERS, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = (data == null) ? new ApiResponse(message) : new ApiResponse(message, data);
        System.out.println("API Response :: " + apiResponse);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
