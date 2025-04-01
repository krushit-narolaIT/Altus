package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.dto.UserDTO;

public class LogOutValidator {
    public static void validateLogOut(UserDTO userDTO) throws ValidationException {
        if(userDTO == null){
            throw new ValidationException(Message.User.INVALID_OPERATION);
        }
        if (String.valueOf(userDTO.getUserId()).isEmpty()) {
            throw new ValidationException(Message.User.INVALID_OPERATION);
        }
    }
}
