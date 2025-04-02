package com.krushit.controller.customer;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.RideValidator;
import com.krushit.dto.*;
import com.krushit.model.RideRequest;
import com.krushit.model.User;
import com.krushit.service.UserService;
import com.krushit.service.VehicleRideService;
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

@WebServlet(value = "/bookRide")
public class BookRideController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final UserService userService = new UserService();
    private final Mapper mapper =Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateCustomerRole(user);
            userService.userBlocked(user.getUserId());
            RideRequestDTO rideRequestDTO = ObjectMapperUtils.toObject(request.getReader(), RideRequestDTO.class);
            rideRequestDTO = setUserId(rideRequestDTO, user.getUserId());
            RideValidator.validateRideRequest(rideRequestDTO);
            RideRequest rideRequest = mapper.toRideRequest(rideRequestDTO);
            vehicleRideService.bookRide(rideRequest);
            createResponse(response, Message.Ride.RIDE_REQUEST_SUBMITTED_SUCCESSFULLY, null, HttpServletResponse.SC_OK);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private RideRequestDTO setUserId(RideRequestDTO rideRequestDTO, int userId){
        return new RideRequestDTO.RideRequestDTOBuilder()
                .setPickUpLocationId(rideRequestDTO.getPickUpLocationId())
                .setDropOffLocationId(rideRequestDTO.getDropOffLocationId())
                .setVehicleServiceId(rideRequestDTO.getVehicleServiceId())
                .setRideDate(rideRequestDTO.getRideDate())
                .setPickUpTime(rideRequestDTO.getPickUpTime())
                .setUserId(userId)
                .build();
    }
}
