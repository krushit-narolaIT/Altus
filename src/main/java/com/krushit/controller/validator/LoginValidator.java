package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.entity.User;

public class LoginValidator {
    public static void validateLoginCredentials(User user) throws ValidationException {
        if (user.getEmailId() == null || user.getEmailId().trim().isEmpty() || user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new ValidationException(Message.User.EMAIL_AND_PASS_REQUIRED);
        }
    }
}
