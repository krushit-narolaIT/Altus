package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    private static final Mapper mapper = Mapper.getInstance();

    private SessionUtils() {
    }

    public static User validateSession(HttpServletRequest request) throws ApplicationException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new ApplicationException(Message.Auth.SESSION_EXPIRED + Message.Auth.PLEASE_LOGIN);
        }
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO == null) {
            throw new ApplicationException(Message.Auth.PLEASE_LOGIN);
        }
        return mapper.convertToEntityUserDTO(userDTO);
    }
}
