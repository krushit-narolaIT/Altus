package com.krushit.controller.customer;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.controller.validator.RideValidator;
import com.krushit.dto.DistanceCalculatorDTO;
import com.krushit.dto.RideServiceDTO;
import com.krushit.entity.User;
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
import java.util.List;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/rideRequest")
public class RequestRideController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateCustomerRole(user);
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
}
