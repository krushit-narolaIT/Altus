package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponse;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.model.Vehicle;
import com.krushit.service.VehicleRideService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.controller.validator.VehicleServicesValidator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddVehicleController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final Mapper mapper = new Mapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                createResponse(response, Message.INVALID_CONTENT_TYPE, null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            HttpSession session = request.getSession();
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            AuthValidator.userLoggedIn(userDTO);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_DRIVER.getRoleName());
            Vehicle vehicle = ObjectMapperUtils.toObject(request.getReader(), Vehicle.class);
            VehicleServicesValidator.validateVehicleDetails(vehicle);
            vehicleRideService.addVehicle(vehicle, user.getUserId());
            createResponse(response, Message.Vehicle.VEHICLE_REGISTERED_SUCCESSFULLY, null, HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.INTERNAL_SERVER_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponse));
    }
}
