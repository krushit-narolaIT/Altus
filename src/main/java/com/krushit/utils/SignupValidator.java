package com.krushit.utils;



import com.krushit.common.Message;
import com.krushit.model.Driver;
import com.krushit.model.User;
import com.krushit.exception.ValidationException;

public class SignupValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,20}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{8,20}$";
    private static final String PHONE_REGEX = "^\\d{10}$";

    public static void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException(Message.User.ENTER_VALID_USER_DATA);
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new ValidationException(Message.User.PLEASE_ENTER_FIRST_NAME);
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new ValidationException(Message.User.PLEASE_ENTER_LASE_NAME);
        }
        if (user.getPhoneNo() == null || user.getPhoneNo().isEmpty()) {
            throw new ValidationException(Message.User.PLEASE_ENTER_PHONE_NO);
        }
        if (user.getEmailId() == null || user.getEmailId().isEmpty()) {
            throw new ValidationException(Message.User.PLEASE_ENTER_EMAIL);
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new ValidationException(Message.User.PLEASE_ENTER_PASSWORD);
        }

        validatePhoneNumber(user.getPhoneNo());
        validateEmail(user.getEmailId());
        validatePassword(user.getPassword());
    }

    public static void validatePassword(String password) throws ValidationException {
        if (password == null || password.length() < 8 || password.length() > 20 || !password.matches(PASSWORD_REGEX)) {
            throw new ValidationException(Message.User.PLEASE_ENTER_VALID_PASSWORD);
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        if (email == null || email.isEmpty() || !email.matches(EMAIL_REGEX)) {
            throw new ValidationException(Message.User.PLEASE_ENTER_VALID_EMAIL);
        }
    }

    public static void validatePhoneNumber(String phoneNumber) throws ValidationException {
        if (phoneNumber == null || phoneNumber.length() != 10 || !phoneNumber.matches(PHONE_REGEX)) {
            throw new ValidationException(Message.User.PLEASE_ENTER_VALID_PHONE_NO);
        }
    }

    public static void validateLoginCredentials(String email, String password) throws ValidationException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new ValidationException(Message.User.EMAIL_AND_PASS_REQUIRED);
        }
    }
}
