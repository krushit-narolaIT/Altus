package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.controller.validator.RideValidator;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.DistanceRequestDTO;
import com.krushit.service.VehicleRideService;
import com.krushit.utils.ObjectMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class DistanceCalculatorController extends HttpServlet {
    private VehicleRideService vehicleRideService = new VehicleRideService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            DistanceRequestDTO distanceRequest = ObjectMapperUtil.toObject(request.getReader(), DistanceRequestDTO.class);
            RideValidator.validateLocation(distanceRequest);
            double distance = vehicleRideService.calculateDistance(distanceRequest.getFrom(), distanceRequest.getTo());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(ObjectMapperUtil.toString(new ApiResponse("Success", distance + " km")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ObjectMapperUtil.toString(new ApiResponse("Error calculating distance", null)));
        }
    }
}
