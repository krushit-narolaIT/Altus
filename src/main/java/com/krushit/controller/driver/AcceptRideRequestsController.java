package com.krushit.controller.driver;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.entity.User;
import com.krushit.service.UserService;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static com.krushit.utils.ResponseUtils.createResponse;

import java.io.IOException;

@WebServlet(value = "/acceptRide")
public class AcceptRideRequestsController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateDriverRole(user);
            userService.userBlocked(user.getUserId());
            int rideRequestId = Integer.parseInt(request.getParameter("rideRequestId"));
            if (String.valueOf(rideRequestId).trim().isEmpty() || !String.valueOf(rideRequestId).matches("\\d+")) {
                throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_RIDE_ID);
            }
            vehicleRideService.acceptRide(user.getUserId(), rideRequestId);
            createResponse(response, Message.Vehicle.RIDE_ACCEPTED, null, HttpServletResponse.SC_OK);
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
}
