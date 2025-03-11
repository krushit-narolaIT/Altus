package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.model.Driver;
import com.krushit.exception.DBException;
import com.krushit.dto.ApiResponse;
import com.krushit.service.DriverService;
import com.krushit.utils.SignupValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DriverDetailsServlet extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        response.setCharacterEncoding("UTF-8");

        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                createResponse(response, Message.INVALID_CONTENT_TYPE, null, HttpServletResponse.SC_BAD_REQUEST);
            }

            Driver driver = objectMapper.readValue(request.getReader(), Driver.class);

            SignupValidator.validateDriver(driver);

            driverService.storeDriverDetails(driver);

            createResponse(response, Message.Driver.DOCUMENT_STORED_SUCCESSFULLY, driver);
        } catch (IllegalArgumentException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (DBException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    private void createResponse(HttpServletResponse response, String message, Object data) throws IOException {
        createResponse(response, message, data, HttpServletResponse.SC_CREATED);
    }
}
