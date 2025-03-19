package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserDTO;
import com.krushit.model.Driver;
import com.krushit.common.exception.DBException;
import com.krushit.dto.ApiResponse;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.service.DriverService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.controller.validator.DriverDocumentValidator;
import com.krushit.utils.ObjectMapperUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class AddDriverDetailsController extends HttpServlet {
    private final DriverService driverService = new DriverService();
    private final Mapper mapper = new Mapper();
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                createResponse(response, Message.INVALID_CONTENT_TYPE, null, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            HttpSession session = request.getSession(false);
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            AuthValidator.userLoggedIn(userDTO);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_DRIVER.getRoleName());
            Driver driver = ObjectMapperUtil.toObject(request.getReader(), Driver.class);
            DriverDocumentValidator.validateDriver(driver);
            driverService.storeDriverDetails(driver);
            createResponse(response, Message.Driver.DOCUMENT_STORED_SUCCESSFULLY, driver, HttpServletResponse.SC_CREATED);
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
        ApiResponse apiResponse =  new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
