package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponse;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.model.VehicleService;
import com.krushit.service.VehicleRideService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.utils.ObjectMapperUtil;
import com.krushit.controller.validator.VehicleServicesValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddVehicleServiceController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final Mapper mapper = new Mapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            HttpSession session = request.getSession(false);
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            AuthValidator.userLoggedIn(userDTO);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            VehicleService vehicleService = ObjectMapperUtil.toObject(request.getReader(), VehicleService.class);
            VehicleServicesValidator.validateVehicleServiceDetails(vehicleService);
            vehicleRideService.addVehicleService(vehicleService);
            createResponse(response, Message.Vehicle.VEHICLE_SERVICE_ADDED_SUCCESSFULLY, null, HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
