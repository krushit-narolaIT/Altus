package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.dto.DriverVerificationRequest;
import com.krushit.common.exception.ApplicationException;
import com.krushit.dto.UserDTO;
import com.krushit.common.enums.Role;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.controller.validator.DriverServicesValidator;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/verifyDriverDocument")
public class VerifyDriverDocumentController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final Mapper mapper = Mapper.getInstance();
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            UserDTO userDTO = SessionUtils.validateSession(request);
            AuthValidator.userLoggedIn(userDTO);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthUtils.validateAdminRole(user);
            int driverId = Integer.parseInt(request.getParameter("driverId"));
            DriverVerificationRequest verificationRequest = ObjectMapperUtils.toObject(request.getReader(), DriverVerificationRequest.class);
            DriverServicesValidator.validateDriverApprovalRequest(verificationRequest);
            driverService.verifyDriver(verificationRequest, driverId);
            createResponse(response, Message.Driver.VERIFICATION_DONE_SUCCESSFUL, null, HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponseDTO apiResponseDTO =  new ApiResponseDTO(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponseDTO));
    }
}