package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.ApiResponse;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.model.Role;
import com.krushit.model.User;
import com.krushit.service.CustomerService;
import com.krushit.controller.validator.AuthValidator;
import com.krushit.utils.ObjectMapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class GetAllUsersController extends HttpServlet {
    private final CustomerService customerService = new CustomerService();
    private final Mapper mapper = new Mapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            HttpSession session = request.getSession(false);
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            AuthValidator.userLoggedIn(userDTO);
            User user = mapper.convertToEntityUserDTO(userDTO);
            AuthValidator.validateUser(user, Role.ROLE_SUPER_ADMIN.getRoleName());
            List<User> users = customerService.getAllCustomers();
            if (users.isEmpty()) {
                createResponse(response, Message.Customer.NO_CUSTOMER_FOUND, null, HttpServletResponse.SC_OK);
            } else {
                createResponse(response, Message.Customer.SUCCESSFULLY_RETRIEVED_CUSTOMER, users, HttpServletResponse.SC_OK);
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
        ApiResponse apiResponse = new ApiResponse(message, data);
        response.getWriter().write(ObjectMapperUtil.toString(apiResponse));
    }
}
