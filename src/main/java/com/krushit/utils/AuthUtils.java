package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.AuthException;
import com.krushit.dto.UserDTO;
import com.krushit.model.User;

public class AuthUtils {

    private AuthUtils() {
    }

    public static void validateAdminRole(User user) throws AuthException {
        validateUser(user, Role.ROLE_SUPER_ADMIN);
    }

    public static void validateCustomerRole(User user) throws AuthException {
        validateUser(user, Role.ROLE_CUSTOMER);
    }

    public static void validateDriverRole(User user) throws AuthException {
        validateUser(user, Role.ROLE_DRIVER);
    }

    public static void validateUser(User user, Role role) throws AuthException {
        if (user == null || user.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN_FIRST);
        }
        if (user.getRole() != role) {
            throw new AuthException(Message.Auth.UNAUTHORIZED);
        }
    }
}
