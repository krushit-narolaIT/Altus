package com.krushit.common.mapper;

import com.krushit.dto.UserDTO;
import com.krushit.dto.UserSignUpDTO;
import com.krushit.model.Role;
import com.krushit.model.User;

public class Mapper {

    public UserDTO convertToDTO(User user) {
        return fromUserEntity(
                user.getUserId(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNo(),
                user.getEmailId(),
                user.getDisplayId()
        );
    }

    public User fromLoginDTO(UserDTO userDTO) {
        return new User.UserBuilder()
                .setEmailId(userDTO.getEmailId())
                .setPassword(userDTO.getPassword())
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

    public static UserDTO fromSignUp(String firstName, String lastName, String phoneNo, String emailId, String password) {
        return new UserDTO.UserDTOBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPhoneNo(phoneNo)
                .setEmailId(emailId)
                .setPassword(password)
                .build();
    }

    public static UserDTO fromUserEntity(int userId, Role role, String firstName, String lastName, String phoneNo, String emailId, String displayId) {
        return new UserDTO.UserDTOBuilder()
                .setUserId(userId)
                .setRole(role)
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPhoneNo(phoneNo)
                .setEmailId(emailId)
                .setDisplayId(displayId)
                .build();
    }
}
