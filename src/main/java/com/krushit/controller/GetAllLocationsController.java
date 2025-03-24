package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponse;
import com.krushit.model.Location;
import com.krushit.service.LocationService;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class GetAllLocationsController extends HttpServlet {
    private final LocationService locationService = new LocationService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setContentType(Message.APPLICATION_JSON);
        try {
//            ApplicationUtils.validateJsonRequest(request.getContentType());
//            UserDTO userDTO = SessionUtil.validateSession(request);
//            User user = mapper.convertToEntityUserDTO(userDTO);
//            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            List<Location> locations = locationService.getAllLocations();
            createResponse(response, Message.Location.SUCCESSFULLY_RETRIEVED_ALL_LOCATIONS, locations, HttpServletResponse.SC_OK);
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
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponse));
    }
}
