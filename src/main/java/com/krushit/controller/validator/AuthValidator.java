package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.AuthException;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;

public class AuthValidator {
    public static void validateUser(User user, Role role) throws AuthException {
        if (user == null || user.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
        if (user.getRole() != role) {
            throw new AuthException(Message.Auth.UNAUTHORIZED);
        }
    }

    public static void userLoggedIn(UserDTO userDTO) throws AuthException {
        if (userDTO == null || userDTO.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
    }
}
