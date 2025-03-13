package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.dto.ApiResponse;
import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.model.User;
import com.krushit.model.Vehicle;
import com.krushit.service.VehicleRideService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddVehicleController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(Message.APPLICATION_JSON);
        try {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null || !"Driver".equalsIgnoreCase(user.getRole().getRoleName())) {
                createResponse(resp, Message.UNAUTHORIZED, null, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            Vehicle vehicle = objectMapper.readValue(req.getReader(), Vehicle.class);
            if (vehicle.getRegistrationNumber() == null || vehicle.getBrandModelId() == null || vehicle.getYear() == 0) {
                createResponse(resp, "Invalid request! Missing required fields.", null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            vehicleRideService.addVehicle(vehicle, user.getUserId());

            createResponse(resp, "Vehicle registered successfully!", null, HttpServletResponse.SC_OK);

        } catch (DBException e) {
            e.printStackTrace();
            createResponse(resp, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            e.printStackTrace();
            createResponse(resp, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(resp, "Server Error", null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = (data == null) ? new ApiResponse(message) : new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
