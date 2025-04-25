package com.krushit.controller;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserSignUpDTO;
import com.krushit.common.enums.RoleType;
import com.krushit.entity.User;
import com.krushit.common.exception.ApplicationException;
import com.krushit.service.UserService;
import com.krushit.utils.ObjectMapperUtils;
import com.krushit.controller.validator.SignupValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(urlPatterns = {"/userSignUp", "/driverSignUp"})
public class UserSignUpController extends HttpServlet {
    private final UserService userService = new UserService();
    private final Mapper mapper = Mapper.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (!Message.APPLICATION_JSON.equals(request.getContentType())) {
                throw new ApplicationException(Message.INVALID_CONTENT_TYPE);
            }
            UserSignUpDTO userSignUpDTO = ObjectMapperUtils.toObject(request.getReader(), UserSignUpDTO.class);
            RoleType userRoleType = request.getServletPath().equalsIgnoreCase(Message.Customer.CUSTOMER_PATH)
                    ? RoleType.ROLE_CUSTOMER
                    : RoleType.ROLE_DRIVER;
            User user = mapper.convertToEntity(userSignUpDTO, userRoleType);
            SignupValidator.validateUser(user);
            userService.registerUser(user);
            createResponse(response, Message.User.USER_REGISTERED_SUCCESSFULLY, user.getEmailId(), HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            createResponse(response, Message.GENERIC_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ApplicationException e) {
            createResponse(response, e.getMessage(), null, HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            createResponse(response, Message.INTERNAL_SERVER_ERROR, null, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
