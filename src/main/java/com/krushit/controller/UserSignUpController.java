package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserSignUpDTO;
import com.krushit.common.enums.Role;
import com.krushit.model.User;
import com.krushit.common.exception.ApplicationException;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.controller.validator.SignupValidator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSignUpController extends HttpServlet {
    private final CustomerService userService = new CustomerService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                throw new ApplicationException(Message.INVALID_CONTENT_TYPE);
            }
            UserSignUpDTO userSignUpDTO = ObjectMapperUtils.toObject(request.getReader(), UserSignUpDTO.class);
            Role userRole = request.getServletPath().equalsIgnoreCase(Message.Customer.CUSTOMER_PATH)
                    ? Role.ROLE_CUSTOMER
                    : Role.ROLE_DRIVER;
            User user = mapper.convertToEntity(userSignUpDTO, userRole);
            SignupValidator.validateUser(user);
            userService.registerUser(user);
            createResponse(response, Message.User.USER_REGISTERED_SUCCESSFULLY, user.getEmailId(), HttpServletResponse.SC_OK);
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
