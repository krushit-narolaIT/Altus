package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    private SessionUtils(){
    }
    public static UserDTO validateSession(HttpServletRequest request) throws ApplicationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ApplicationException(Message.Auth.SESSION_EXPIRED + Message.Auth.PLEASE_LOGIN_FIRST);
        }
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO == null) {
            throw new ApplicationException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
        return userDTO;
    }
}
