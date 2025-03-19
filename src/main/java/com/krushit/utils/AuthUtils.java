package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.common.exception.AuthException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dto.UserDTO;
import com.krushit.model.Role;
import com.krushit.model.User;
import jakarta.servlet.http.HttpSession;

public class AuthUtils {
    private static final Mapper mapper = new Mapper();

    public static User getAuthenticatedUser(HttpSession session, Role requiredRole) throws AuthException {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        userLoggedIn(userDTO);
        User user = mapper.convertToEntityUserDTO(userDTO);
        validateUser(user, requiredRole.getRoleName());
        return user;
    }

    public static void validateUser(User user, String requiredRole) throws AuthException {
        if (user == null || user.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
        if (!user.getRole().getRoleName().equalsIgnoreCase(requiredRole)) {
            throw new AuthException(Message.Auth.UNAUTHORIZED);
        }
    }

    public static void userLoggedIn(UserDTO userDTO) throws AuthException {  //*
        if (userDTO == null || userDTO.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
    }

}
