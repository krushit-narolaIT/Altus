package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.common.enums.RoleType;
import com.krushit.common.exception.AuthException;
import com.krushit.entity.User;

public class AuthUtils {

    private AuthUtils() {
    }

    public static void validateAdminRole(User user) throws AuthException {
        validateUser(user, RoleType.ROLE_SUPER_ADMIN);
    }

    public static void validateCustomerRole(User user) throws AuthException {
        validateUser(user, RoleType.ROLE_CUSTOMER);
    }

    public static void validateDriverRole(User user) throws AuthException {
        validateUser(user, RoleType.ROLE_DRIVER);
    }

    public static void validateUser(User user, RoleType roleType) throws AuthException {
        if (user == null || user.getRole() == null) {
            throw new AuthException(Message.Auth.PLEASE_LOGIN);
        }
        if (user.getRole().getRoleType() != roleType) {
            throw new AuthException(Message.Auth.UNAUTHORIZED);
        }
    }
}
