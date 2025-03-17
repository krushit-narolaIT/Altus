package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.AuthException;
import com.krushit.model.User;

public class AuthValidator {
    public static void validateUser(User user, String requiredRole) throws AuthException {
        if (user == null || user.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
        if (!user.getRole().getRoleName().equalsIgnoreCase(requiredRole)) {
            throw new AuthException(Message.Auth.UNAUTHORIZED);
        }
    }
}
