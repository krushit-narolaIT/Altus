package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.entity.User;
import com.krushit.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class UserLoginServlet extends HttpServlet {
	private final UserServiceImpl userService = new UserServiceImpl();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("emailId");
		String password = request.getParameter("password");

		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			response.setStatus(400);
			response.getWriter().write("{\"error\": \"Email and password are required\"}");
			return;
		}

		User authenticatedUser = userService.userLogin(email, password);

		if (authenticatedUser != null) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", authenticatedUser);

			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/json");
			new ObjectMapper().writeValue(response.getWriter(), authenticatedUser);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"error\": \"Invalid email or password\"}");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		resp.getWriter().write("{\"error\": \"GET method not allowed for login\"}");
	}
}
