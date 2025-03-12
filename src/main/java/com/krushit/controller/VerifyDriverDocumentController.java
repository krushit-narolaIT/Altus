package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.DriverVerificationRequest;
import com.krushit.exception.ApplicationException;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class VerifyDriverDocumentController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");

        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user == null || user.getRole().getRoleId() != 1) {
                createResponse(response, Message.UNAUTHORIZED, null, HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            DriverVerificationRequest verificationRequest = objectMapper.readValue(request.getReader(), DriverVerificationRequest.class);

            if (verificationRequest.getDriverId() == null || verificationRequest.getVerificationStatus() == null) {
                createResponse(response, "Invalid request! Missing required fields.", null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (!driverService.isDriverExist(verificationRequest.getDriverId())){
                createResponse(response, "Driver not found!", null, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if ("ACCEPT".equalsIgnoreCase(verificationRequest.getVerificationStatus())) {
                driverService.verifyDriver(verificationRequest.getDriverId(), true, null);
            } else if ("REJECT".equalsIgnoreCase(verificationRequest.getVerificationStatus())) {
                driverService.verifyDriver(verificationRequest.getDriverId(), false, verificationRequest.getMessage());
            } else {
                createResponse(response, "Invalid verification status! Use 'ACCEPT' or 'REJECT'.", null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            createResponse(response, Message.Driver.VERIFICATION_DONE_SUCCESSFUL, null, HttpServletResponse.SC_OK);
        } catch (ApplicationException e) {
            e.printStackTrace();
            createResponse(response, Message.DRIVER_NOT_EXIST, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (Exception e) {
            e.printStackTrace();
            createResponse(response, "Server Error", null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = (data == null) ? new ApiResponse(message) : new ApiResponse(message, data);
        System.out.println("API Response :: " + apiResponse);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
