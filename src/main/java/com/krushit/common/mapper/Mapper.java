package com.krushit.common.mapper;

import com.krushit.dto.UserDTO;
import com.krushit.model.User;

public class Mapper {
    public UserDTO convertToDTO(User user) {
        return new UserDTO(user.getUserId(), user.getRole().getRoleName(), user.getFirstName(), user.getLastName(),
                user.getPhoneNo(), user.getEmailId(), user.getDisplayId());
    }

    public User convertToEntity(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getRole(), userDTO.getFirstName(), userDTO.getLastName(),
                userDTO.getPhoneNo(), userDTO.getEmailId(), userDTO.getDisplayId());
    }
}
