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

import static com.krushit.utils.ResponseUtils.createResponse;

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
            createResponse(response, Message.User.LOGIN_SUCCESSFUL, null, HttpServletResponse.SC_OK);
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