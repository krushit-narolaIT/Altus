package com.krushit.controller.customer_controller;

import com.krushit.common.Message;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.dto.*;
import com.krushit.model.User;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(value = "/getAllCustomerRides")
public class GetAllRidesController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            UserDTO userDTO = SessionUtils.validateSession(request);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_CUSTOMER.getRoleName());
            List<RideDTO> allPreviousRides = vehicleRideService.getAllRides(user.getUserId(), false);
            createResponse(response, Message.Ride.RIDES_FETCHED_SUCCESSFULLY, allPreviousRides, HttpServletResponse.SC_OK);
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
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponseDTO));
    }
}
