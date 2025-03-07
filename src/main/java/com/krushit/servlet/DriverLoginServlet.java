package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.common.Message;
import com.krushit.entity.User;
import com.krushit.model.ApiResponse;
import com.krushit.service.DriverServiceImpl; // ✅ Use DriverServiceImpl
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class DriverLoginServlet extends HttpServlet {
	private final DriverServiceImpl driverService = new DriverServiceImpl();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		User loginUser = objectMapper.readValue(request.getReader(), User.class);
		String email = loginUser.getEmailId();
		String password = loginUser.getPassword();

		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			sendResponse(response, HttpServletResponse.SC_BAD_REQUEST, Message.EMAIL_AND_PASS_REQUIRED, null);
			return;
		}

		User authenticatedDriver = driverService.driverLogin(email, password);
		if (authenticatedDriver != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("driver", authenticatedDriver);

			sendResponse(response, HttpServletResponse.SC_OK, Message.LOGIN_SUCCESSFUL, authenticatedDriver);
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
