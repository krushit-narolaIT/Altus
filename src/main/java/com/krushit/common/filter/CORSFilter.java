package com.krushit.common.filter;

import com.krushit.common.Message;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String origin = request.getHeader("Origin");

        if (origin != null) {
            response.setHeader(Message.CORSConstants.HEADER_ALLOW_ORIGIN, origin);
            response.setHeader("Vary", "Origin");
        }
        //response.setHeader(Message.CORSConstants.HEADER_ALLOW_ORIGIN, Message.CORSConstants.VALUE_ALLOW_ALL);
        response.setHeader(Message.CORSConstants.HEADER_ALLOW_METHODS, Message.CORSConstants.VALUE_ALLOW_METHODS);
        response.setHeader(Message.CORSConstants.HEADER_ALLOW_HEADERS, Message.CORSConstants.VALUE_ALLOW_HEADERS);
        response.setHeader(Message.CORSConstants.HEADER_ALLOW_CREDENTIALS, Message.CORSConstants.VALUE_ALLOW_CREDENTIALS);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
