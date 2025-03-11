package com.krushit.servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.entity.Driver;
import com.krushit.model.ApiResponse;
import com.krushit.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DriverVerificationServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.INVALID_CONTENT_TYPE);
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                createResponse(response, Message.INVALID_CONTENT_TYPE, null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            BufferedReader reader = request.getReader();
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(requestBody.toString());

            int roleId = jsonNode.has("roleId") ? jsonNode.get("roleId").asInt() : -1;
            System.out.println("Role ID: " + roleId);

            if (roleId != 1) {
                createResponse(response, Message.UNAUTHORIZED, null, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            System.out.println("In doGet");

            List<Driver> pendingDrivers = driverService.getPendingVerificationDrivers();

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
        ApiResponse apiResponse = (data == null) ? new ApiResponse(message) : new ApiResponse(message, data);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private void createResponse(HttpServletResponse response, String message, Object data) throws IOException {
        createResponse(response, message, data, HttpServletResponse.SC_CREATED);
    }
}
