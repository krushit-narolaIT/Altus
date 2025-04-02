package com.krushit.controller.driver;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.dto.RideResponseDTO;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import com.krushit.service.UserService;
import com.krushit.service.VehicleRideService;
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

@WebServlet(value = "/getAllRideRequest")
public class GetAllRideRequestsController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateDriverRole(user);
            userService.userBlocked(user.getUserId());
            List<RideResponseDTO> rideRequestDTOList = vehicleRideService.getAllRequest(user.getUserId());
            if(rideRequestDTOList.isEmpty()){
                createResponse(response, Message.Vehicle.NO_REQUEST_FOUND, null, HttpServletResponse.SC_OK);
            }
            createResponse(response, Message.Vehicle.FETCHING_ALL_REQUEST_SUCCESSFULLY, rideRequestDTOList, HttpServletResponse.SC_OK);
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
