package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UpdatePasswordDTO;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import com.krushit.utils.ObjectMapperUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(value = "/updatePassword")
public class UpdatePasswordController extends HttpServlet {
    private final CustomerService userService = new CustomerService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not logged in", null);
                return;
            }

            UpdatePasswordDTO updatePasswordDTO = ObjectMapperUtils.toObject(request.getReader(), UpdatePasswordDTO.class);
            String email = (String) session.getAttribute("email");

            userService.updatePassword(email, updatePasswordDTO.getOldPassword(), updatePasswordDTO.getNewPassword());

            sendResponse(response, HttpServletResponse.SC_OK, "Password updated successfully", null);
        } catch (ApplicationException e) {
            sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), null);
        } catch (DBException e) {
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error", null);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unexpected error", null);
        }
    }

    private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
        response.setStatus(statusCode);
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponse));
    }
}
