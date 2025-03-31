package com.krushit.controller.admin_controller;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.common.enums.Role;
import com.krushit.model.User;
import com.krushit.model.VehicleService;
import com.krushit.service.VehicleRideService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.controller.validator.VehicleServicesValidator;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/addVehicleService")
public class AddVehicleServiceController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            UserDTO userDTO = SessionUtils.validateSession(request);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            VehicleService vehicleService = ObjectMapperUtils.toObject(request.getReader(), VehicleService.class);
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
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponseDTO));
    }
}
