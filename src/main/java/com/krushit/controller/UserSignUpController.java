package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.exception.DBException;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.exception.ApplicationException;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import com.krushit.utils.ObjectMapperUtil;
import com.krushit.utils.SignupValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSignUpController extends HttpServlet {
    private CustomerService userService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                throw new ApplicationException(Message.INVALID_CONTENT_TYPE);
            }
            User user = ObjectMapperUtil.toObject(request.getReader(), User.class);
            SignupValidator.validateUser(user);
            if(request.getServletPath().equalsIgnoreCase(Message.Customer.CUSTOMER_PATH)){
                user.setRole(Role.ROLE_CUSTOMER);
            } else {
                user.setRole(Role.ROLE_DRIVER);
            }
            userService.registerUser(user);
            createResponse(response, Message.User.USER_REGISTERED_SUCCESSFULLY, user.getEmailId(), HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        }  catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.INTERNAL_SERVER_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
