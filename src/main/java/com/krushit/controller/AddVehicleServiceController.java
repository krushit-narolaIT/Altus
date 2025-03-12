package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.dto.ApiResponse;
import com.krushit.exception.ApplicationException;
import com.krushit.model.User;
import com.krushit.model.VehicleService;
import com.krushit.service.VehicleRideService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddVehicleServiceController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);

        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null || !user.getRole().getRoleName().equalsIgnoreCase("Admin")) {
                createResponse(response, Message.UNAUTHORIZED, null, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            VehicleService newVehicleService = objectMapper.readValue(request.getReader(), VehicleService.class);

            if (newVehicleService.getServiceName() == null || newVehicleService.getVehicleType() == null) {
                createResponse(response, "Invalid request! Missing required fields.", null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            vehicleRideService.addVehicleService(newVehicleService);

            createResponse(response, "Vehicle service added successfully!", null, HttpServletResponse.SC_OK);
        } catch (ApplicationException e) {
            e.printStackTrace();
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, "Server Error", null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = (data == null) ? new ApiResponse(message) : new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
