package com.krushit.controller.customer;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.entity.User;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/cancelRide")
public class CancelRideController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateCustomerRole(user);
            int rideId = Integer.parseInt(request.getParameter("rideId"));
            vehicleRideService.cancelRide(rideId, user.getUserId(), false);
            createResponse(response, Message.Ride.RIDE_CANCELLED, null, HttpServletResponse.SC_OK);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
