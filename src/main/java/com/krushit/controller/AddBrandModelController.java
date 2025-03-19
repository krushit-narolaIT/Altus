package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponse;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.model.BrandModel;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.service.VehicleRideService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.ObjectMapperUtil;
import com.krushit.controller.validator.VehicleServicesValidator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddBrandModelController extends HttpServlet {
    private final VehicleRideService vehicleRideService = new VehicleRideService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            HttpSession session = request.getSession(false);
            AuthUtils.getAuthenticatedUser(session, Role.ROLE_SUPER_ADMIN);
            BrandModel brandModel = ObjectMapperUtil.toObject(request.getReader(), BrandModel.class);
            VehicleServicesValidator.validateVehicleModelDetails(brandModel);
            vehicleRideService.addBrandModel(brandModel);
            createResponse(response, Message.Vehicle.BRAND_MODEL_ADDED_SUCCESSFULLY, null, HttpServletResponse.SC_OK);
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

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
