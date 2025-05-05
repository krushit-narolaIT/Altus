package com.krushit.controller.driver;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.entity.Driver;
import com.krushit.common.exception.DBException;
import com.krushit.entity.User;
import com.krushit.service.DriverService;
import com.krushit.controller.validator.DriverDocumentValidator;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/addDriverDetails")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50
)
public class AddDriverDetailsController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod())) {
            doPatch(request, response);
        } else {
            super.service(request, response);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = SessionUtils.validateSession(request);
            AuthUtils.validateDriverRole(user);
            String licenceNumber = request.getParameter("licenceNumber");
            Part licencePhoto = request.getPart("licencePhoto");
            String storedPhotoPath = driverService.storeLicencePhoto(licencePhoto, licenceNumber, user.getDisplayId());
            Driver driver = new Driver.DriverBuilder()
                    .setLicenceNumber(licenceNumber)
                    .setLicencePhoto(storedPhotoPath)
                    .setUser(user)
                    .build();
            DriverDocumentValidator.validateDriverDocuments(driver, licencePhoto);
            driverService.storeDriverDetails(driver);
            createResponse(response, Message.Driver.DOCUMENT_STORED_SUCCESSFULLY, null, HttpServletResponse.SC_CREATED);
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
