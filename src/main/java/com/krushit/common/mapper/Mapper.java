package com.krushit.common.mapper;

import com.krushit.common.enums.Role;
import com.krushit.dto.RideDTO;
import com.krushit.dto.RideRequestDTO;
import com.krushit.dto.UserDTO;
import com.krushit.dto.UserSignUpDTO;
import com.krushit.model.Ride;
import com.krushit.model.RideRequest;
import com.krushit.model.User;

public class Mapper {
    private static final Mapper INSTANCE = new Mapper();

    private Mapper() {
    }

    public static Mapper getInstance() {
        return INSTANCE;
    }

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
                .setDisplayId((userDTO.getDisplayId()))
                .build();
    }

    public RideRequest toRideRequest(RideRequestDTO rideRequestDTO) {
        return new RideRequest.RideRequestBuilder()
                .setUserId(rideRequestDTO.getUserId())
                .setPickUpLocationId(rideRequestDTO.getPickUpLocationId())
                .setDropOffLocationId(rideRequestDTO.getDropOffLocationId())
                .setVehicleServiceId(rideRequestDTO.getVehicleServiceId())
                .setRideDate(rideRequestDTO.getRideDate())
                .setPickUpTime(rideRequestDTO.getPickUpTime())
                .build();
    }
}
