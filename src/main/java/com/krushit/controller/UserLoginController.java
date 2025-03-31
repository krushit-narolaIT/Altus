package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.service.UserService;
import com.krushit.controller.validator.LoginValidator;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(value = "/userLogin")
public class UserLoginController extends HttpServlet {
    private final UserService userService = new UserService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            UserDTO loginUser = ObjectMapperUtils.toObject(request.getReader(), UserDTO.class);
            User user = mapper.fromLoginDTO(loginUser);
            LoginValidator.validateLoginCredentials(user);
            UserDTO authenticatedUser = userService.userLogin(user.getEmailId(), user.getPassword());
            HttpSession session = request.getSession(true);
            session.setAttribute("user", authenticatedUser);
            sendResponse(response, HttpServletResponse.SC_OK, Message.User.LOGIN_SUCCESSFUL, authenticatedUser);
        } catch (DBException e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        } catch (ApplicationException e) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, Message.GENERIC_ERROR, null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponseDTO));
    }
}