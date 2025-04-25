package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.controller.validator.DateValidator;
import com.krushit.dto.DateRangeIncomeResponseDTO;
import com.krushit.entity.User;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/getRideDetailsByRange")
public class GetRideDetailsByRangeController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            LocalDate startDate = DateValidator.getLocalDate(request.getParameter("startDate"));
            LocalDate endDate = DateValidator.getLocalDate(request.getParameter("endDate"));
            DateRangeIncomeResponseDTO dateRangeIncomeResponseDTO = vehicleRideService.getIncomeByDateRange(0, startDate, endDate);
            createResponse(response, Message.Vehicle.VEHICLE_REGISTERED_SUCCESSFULLY, dateRangeIncomeResponseDTO, HttpServletResponse.SC_OK);
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
