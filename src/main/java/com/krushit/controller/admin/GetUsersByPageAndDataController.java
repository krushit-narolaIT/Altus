package com.krushit.controller.admin;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.User;
import com.krushit.service.UserService;
import com.krushit.utils.AuthUtils;
import com.krushit.utils.UserContextUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static com.krushit.utils.ResponseUtils.createResponse;

@WebServlet(value = "/getUsersByPageAndData")
public class GetUsersByPageAndDataController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(Message.APPLICATION_JSON);
        try {
            User user = UserContextUtils.getUser();
            AuthUtils.validateAdminRole(user);
            int page = Integer.parseInt(request.getParameter("page"));
            int dataPerPage = Integer.parseInt(request.getParameter("data"));
            List<User> users = userService.getCustomersByPage(page, dataPerPage);
            if (users.isEmpty()) {
                createResponse(response, Message.Customer.NO_CUSTOMER_FOUND, null, HttpServletResponse.SC_NO_CONTENT);
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
}
