package com.krushit.controller.admin_controller;

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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(value = "/getUserById")
public class GetUserByIdController extends HttpServlet {
    private final UserService userService = new UserService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            ApplicationUtils.validateJsonRequest(request.getContentType());
            UserDTO userDTO = SessionUtils.validateSession(request);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            int userId = Integer.parseInt(request.getParameter("userId"));
            Optional<User> userOpt = userService.getUserDetails(userId);
            if(!userOpt.isPresent()){
                createResponse(response, Message.Customer.NO_CUSTOMER_FOUND, null, HttpServletResponse.SC_OK);
            } else {
                createResponse(response, Message.Customer.SUCCESSFULLY_RETRIEVED_CUSTOMER, userOpt.get(), HttpServletResponse.SC_OK);
            }
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void createResponse(HttpServletResponse response, String message, Object data, int statusCode) throws IOException {
        response.setStatus(statusCode);
        ApiResponseDTO apiResponseDTO = new ApiResponseDTO(message, data);
        response.getWriter().write(ObjectMapperUtils.toString(apiResponseDTO));
    }
}
