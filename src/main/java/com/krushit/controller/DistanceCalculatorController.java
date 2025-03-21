package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.controller.validator.RideValidator;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.DistanceCalculatorDTO;
import com.krushit.dto.UserDTO;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.service.LocationService;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class DistanceCalculatorController extends HttpServlet {
    private final LocationService locationService = new LocationService();
    private final Mapper mapper = new Mapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            HttpSession session = request.getSession(false);
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            AuthValidator.userLoggedIn(userDTO);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            DistanceCalculatorDTO distanceRequest = ObjectMapperUtils.toObject(request.getReader(), DistanceCalculatorDTO.class);
            RideValidator.validateLocation(distanceRequest);
            double distance = locationService.calculateDistance(distanceRequest.getFrom(), distanceRequest.getTo());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(ObjectMapperUtils.toString(new ApiResponse("Success", distance + " km")));
        } catch (DBException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ObjectMapperUtils.toString(new ApiResponse(Message.GENERIC_ERROR, null)));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(ObjectMapperUtils.toString(new ApiResponse("Error calculating distance", null)));
        }
    }
}
