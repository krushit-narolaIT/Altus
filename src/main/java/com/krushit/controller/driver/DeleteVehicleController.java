package com.krushit.controller.driver;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import com.krushit.service.UserService;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/deleteVehicle")
public class DeleteVehicleController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final UserService userService = new UserService();

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateDriverRole(user);
            userService.userBlocked(user.getUserId());
            driverService.deleteVehicle(user.getUserId());
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
}
