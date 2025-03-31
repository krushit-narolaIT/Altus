package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import com.krushit.service.UserService;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/updateUser")
public class UserUpdateController extends HttpServlet {
    private final UserService userService = new UserService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            UserDTO userDTO = SessionUtils.validateSession(request);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_CUSTOMER.getRoleName());
            UserDTO updatedUser = ObjectMapperUtils.toObject(request.getReader(), UserDTO.class);
            userService.updateUser(updatedUser, user.getUserId());
            sendResponse(response, HttpServletResponse.SC_OK, "User updated successfully", null);
        } catch (DBException e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", null);
        } catch (ApplicationException e) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong", null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponseDTO));
    }
}
