package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.ApiResponseDTO;
import com.krushit.dto.ChangePasswordDTO;
import com.krushit.service.UserService;
import com.krushit.utils.ApplicationUtils;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/changePassword")
public class ChangePasswordController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            ChangePasswordDTO changePasswordDTO = ObjectMapperUtils.toObject(request.getReader(), ChangePasswordDTO.class);
            userService.updatePassword(changePasswordDTO.getEmailId(), changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
            sendResponse(response, HttpServletResponse.SC_OK, Message.User.PASSWORD_CHANGED_SUCCESSFULLY, null);
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
