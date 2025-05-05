package com.krushit.controller.admin;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.entity.User;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/getBrandModelByBrand")
public class GetBrandModelByBrandController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            String brand = request.getParameter("brand");
            List<String> allModels = vehicleRideService.getModelsByBrand(brand);
            if (!allModels.isEmpty()) {
                createResponse(response, Message.Vehicle.SUCCESSFULLY_RETRIEVED_BRANDS, allModels, HttpServletResponse.SC_OK);
            } else {
                createResponse(response, Message.Vehicle.NO_BRANDS_FOUND, null, HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
            createResponse(response, Message.Ride.PLEASE_ENTER_VALID_RIDE_FORMAT, null, HttpServletResponse.SC_BAD_REQUEST);
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
}
