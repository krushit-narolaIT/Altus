package com.krushit.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krushit.entity.User;
import com.krushit.entity.UserBuilder;
import com.krushit.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

@WebServlet(value = "/UserSignUp")
public class UserSignUpServlet extends HttpServlet {
	private final UserServiceImpl userService = new UserServiceImpl();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Read request body
		InputStream inputStream = request.getInputStream();
		if (inputStream == null || inputStream.available() == 0) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("{\"error\": \"Empty request body\"}");
			return;
		}

		// Convert JSON to User object
		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(inputStream, User.class);

		// Register user
		boolean isRegistered = userService.registerUser(user);
		if (isRegistered) {
			response.setStatus(HttpServletResponse.SC_CREATED);
			response.getWriter().write("{\"message\": \"User registered successfully\"}");
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"User registration failed\"}");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
