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

import static com.krushit.utils.ResponseUtils.createResponse;

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
            createResponse(response, Message.User.PASSWORD_CHANGED_SUCCESSFULLY, null, HttpServletResponse.SC_OK);
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
