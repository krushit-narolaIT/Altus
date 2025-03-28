package com.krushit.controller.driver_controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.DateValidator;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.DateRangeIncomeDTO;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(value = "/getIncomeByRange")
public class GetMonthlyIncomeController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        try {
//            UserDTO userDTO = SessionUtils.validateSession(request);
//            User user = mapper.convertToEntityUserDTO(userDTO);
//            AuthValidator.validateUser(user, Role.ROLE_DRIVER.getRoleName());
            int userid = 12;
            LocalDate startDate = DateValidator.getLocalDate(request.getParameter("startDate"));
            LocalDate endDate = DateValidator.getLocalDate(request.getParameter("endDate"));
            DateRangeIncomeDTO dateRangeIncomeDTO = vehicleRideService.getIncomeByDateRange(userid, startDate, endDate);
            createResponse(response, Message.Vehicle.VEHICLE_REGISTERED_SUCCESSFULLY, dateRangeIncomeDTO, HttpServletResponse.SC_OK);
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

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponse));
    }
}
