package com.krushit.common.mapper;

import com.krushit.dto.UserDTO;
import com.krushit.dto.UserSignUpDTO;
import com.krushit.model.Role;
import com.krushit.model.User;

public class Mapper {
    public UserDTO convertToDTO(User user) {
        return new UserDTO.UserDTOBuilder()
                .setUserId(user.getUserId())
                .setRole(user.getRole())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNo(user.getPhoneNo())
                .setEmailId(user.getEmailId())
                .setDisplayId(user.getDisplayId())
                .build();
    }

    public User convertToEntity(UserSignUpDTO userSignUpDTO, Role role) {
        return new User.UserBuilder()
                .setFirstName(userSignUpDTO.getFirstName())
                .setLastName(userSignUpDTO.getLastName())
                .setPhoneNo(userSignUpDTO.getPhoneNo())
                .setEmailId(userSignUpDTO.getEmailId())
                .setPassword(userSignUpDTO.getPassword())
                .setRole(role)
                .build();
    }

    public User convertToEntityUserDTO(UserDTO userDTO) {
        return new User.UserBuilder()
                .setUserId(userDTO.getUserId())
                .setRole(userDTO.getRole())
                .setFirstName(userDTO.getFirstName())
                .setLastName(userDTO.getLastName())
                .setPhoneNo(userDTO.getPhoneNo())
                .setEmailId(userDTO.getEmailId())
                .build();
    }
}
