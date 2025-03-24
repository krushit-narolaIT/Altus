package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.RideValidator;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.DistanceCalculatorDTO;
import com.krushit.dto.RideServiceDTO;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class RequestRideController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
//                ApplicationUtils.validateJsonRequest(request.getContentType());
//
//                UserDTO userDTO = SessionUtil.validateSession(request);
//                User user = mapper.convertToEntityUserDTO(userDTO);
//                AuthValidator.validateUser(user, Role.ROLE_CUSTOMER.getRoleName());
            DistanceCalculatorDTO distanceCalculatorDTO = ObjectMapperUtils.toObject(request.getReader(), DistanceCalculatorDTO.class);
            RideValidator.validateLocation(distanceCalculatorDTO);
            List<RideServiceDTO> rideOptions = vehicleRideService.getAvailableRides(
                    distanceCalculatorDTO.getFrom(),
                    distanceCalculatorDTO.getTo()
            );
            createResponse(response, Message.Ride.RIDE_SERVICES_FETCHED_SUCCESSFULLY, rideOptions, HttpServletResponse.SC_OK);
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
