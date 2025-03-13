package com.krushit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krushit.common.Message;
import com.krushit.model.User;
import com.krushit.dto.ApiResponse;
import com.krushit.service.CustomerService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class UserLoginController extends HttpServlet {
	private final CustomerService userService = new CustomerService();
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType(Message.APPLICATION_JSON);
		User loginUser = objectMapper.readValue(request.getReader(), User.class);
		String email = loginUser.getEmailId();
		String password = loginUser.getPassword();
		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.EMAIL_AND_PASS_REQUIRED, null);
			return;
		}
		User authenticatedUser = userService.userLogin(email, password);
		if (authenticatedUser != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", authenticatedUser);
			sendResponse(response, HttpServletResponse.SC_OK, Message.LOGIN_SUCCESSFUL, authenticatedUser);
		} else {
			sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, Message.INVALID_EMAIL_AND_PASS, null);
		}
	}

	private void sendResponse(HttpServletResponse response, int statusCode, String message, Object data) throws IOException {
		response.setStatus(statusCode);
		ApiResponse apiResponse = new ApiResponse(message, data);
		response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
	}
}