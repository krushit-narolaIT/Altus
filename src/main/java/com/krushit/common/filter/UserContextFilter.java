package com.krushit.common.filter;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.model.User;
import static com.krushit.utils.ResponseUtils.createResponse;
import com.krushit.utils.SessionUtils;
import com.krushit.utils.UserContextUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//@WebFilter("/*")
public class UserContextFilter implements Filter {
    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/userLogin"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = null;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        boolean isExcluded = EXCLUDED_PATHS.stream().anyMatch(path -> path.equals(httpServletRequest.getServletPath()));
        try {
            if (!isExcluded) {
                httpServletResponse = (HttpServletResponse) response;
                User user = SessionUtils.validateSession(httpServletRequest);
                UserContextUtils.setUser(user);
            }
            filterChain.doFilter(request, response);
        } catch (ApplicationException e) {
            createResponse(httpServletResponse, Message.Auth.PLEASE_LOGIN, null, HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            UserContextUtils.clear();
        }
    }
}
