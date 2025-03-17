package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.dto.ApiResponse;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.Location;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.service.LocationService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.utils.ObjectMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddLocationsController extends HttpServlet {
    private LocationService locationService = new LocationService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                createResponse(response, Message.INVALID_CONTENT_TYPE, null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            Location location = ObjectMapperUtil.toObject(request.getReader(), Location.class);
            locationService.addLocation(location.getName());
            createResponse(response, Message.Location.LOCATION_ADDED_SUCCESSFULLY, null, HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = (data == null) ? new ApiResponse(message) : new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
