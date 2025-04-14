package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Location;
import com.krushit.entity.User;
import com.krushit.service.LocationService;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/addLocation")
public class AddLocationsController extends HttpServlet {
    private final LocationService locationService = new LocationService();
    private final Mapper mapper = Mapper.getInstance();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            Location location = ObjectMapperUtils.toObject(request.getReader(), Location.class);
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
}
