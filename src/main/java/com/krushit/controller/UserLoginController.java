package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.exception.ValidationException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import com.krushit.controller.validator.LoginValidator;
import com.krushit.utils.ObjectMapperUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class UserLoginController extends HttpServlet {
    private final CustomerService userService = new CustomerService();
    private final Mapper mapper = new Mapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User loginUser = ObjectMapperUtil.toObject(request.getReader(), User.class);
            LoginValidator.validateLoginCredentials(loginUser);
            String email = loginUser.getEmailId();
            String password = loginUser.getPassword();
            UserDTO authenticatedUser = userService.userLogin(email, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("user", authenticatedUser);
            sendResponse(response, HttpServletResponse.SC_OK, Message.User.LOGIN_SUCCESSFUL, authenticatedUser);
        } catch (ValidationException e) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (DBException e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        } catch (ApplicationException e) {
            sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}