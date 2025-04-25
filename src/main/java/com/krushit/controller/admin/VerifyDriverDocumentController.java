package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.dto.DriverVerificationRequestDTO;
import com.krushit.common.exception.ApplicationException;
import com.krushit.entity.User;
import com.krushit.service.DriverService;
import com.krushit.controller.validator.DriverServicesValidator;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/verifyDriverDocument")
public class VerifyDriverDocumentController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            int driverId = Integer.parseInt(request.getParameter("driverId"));
            DriverVerificationRequestDTO verificationRequest = ObjectMapperUtils.toObject(request.getReader(), DriverVerificationRequestDTO.class);
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
}