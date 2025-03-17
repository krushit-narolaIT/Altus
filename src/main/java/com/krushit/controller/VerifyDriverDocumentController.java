package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.dto.ApiResponse;
import com.krushit.dto.DriverVerificationRequest;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.controller.validator.DriverServicesValidator;
import com.krushit.utils.ObjectMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class VerifyDriverDocumentController extends HttpServlet {
    private DriverService driverService = new DriverService();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            DriverVerificationRequest verificationRequest = ObjectMapperUtil.toObject(request.getReader(), DriverVerificationRequest.class);
            DriverServicesValidator.validateDriverApprovalRequest(verificationRequest);
            if (!driverService.isDriverExist(verificationRequest.getDriverId())){
                createResponse(response, Message.Driver.DRIVER_NOT_EXIST, null, HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if ("ACCEPT".equalsIgnoreCase(verificationRequest.getVerificationStatus())) {
                driverService.verifyDriver(verificationRequest.getDriverId(), true, null);
            } else {
                driverService.verifyDriver(verificationRequest.getDriverId(), false, verificationRequest.getMessage());
            }
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
        ApiResponse apiResponse =  new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
