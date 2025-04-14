package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Driver;
import com.krushit.entity.User;
import com.krushit.service.DriverService;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/getAllPendingVerification")
public class GetAllPendingDriverVerificationController extends HttpServlet {
    private final DriverService driverService = new DriverService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.INVALID_CONTENT_TYPE);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateAdminRole(user);
            List<Driver> pendingDrivers = driverService.getPendingVerificationDrivers();
            if (pendingDrivers.isEmpty()) {
                createResponse(response, Message.Driver.NO_PENDING_VERIFICATION, null, HttpServletResponse.SC_OK);
            } else {
                createResponse(response, Message.Driver.SUCCESSFULLY_RETRIEVE_DRIVERS, pendingDrivers, HttpServletResponse.SC_OK);
            }
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
