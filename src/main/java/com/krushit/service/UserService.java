package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.mapper.Mapper;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.dto.UserDTO;
import com.krushit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {
    private final IUserDAO userDAO = new UserDAOImpl();

    public void userBlocked(int userId) throws ApplicationException {
        if (userDAO.isUserBlocked(userId)) {
            throw new ApplicationException(Message.User.YOUR_ACCOUNT_IS_SUSPENDED_PLEASE_CONTACT_SUPPORT);
        }
    }

    public void registerUser(User user) throws ApplicationException {
        if (userDAO.isUserExist(user.getEmailId(), user.getPhoneNo())) {
            throw new ApplicationException(Message.USER_ALREADY_EXIST);
        }
        userDAO.registerUser(user);
    }

    public UserDTO userLogin(String email, String password) throws ApplicationException {
        if (!userDAO.isValidUser(email, password)) {
            throw new ApplicationException(Message.User.PLEASE_ENTER_VALID_EMAIL_OR_PASS);
        }
        Mapper mapper = Mapper.getInstance();
        return mapper.convertToDTO(userDAO.getUser(email, password));
    }

    public List<UserDTO> getAllCustomers() throws ApplicationException {
        List<User> users = userDAO.getAllCustomers();
        return users.stream()
                .map(user -> new UserDTO.UserDTOBuilder()
                        .setUserId(user.getUserId())
                        .setRole(user.getRole())
                        .setFirstName(user.getFirstName())
                        .setLastName(user.getLastName())
                        .setPhoneNo(user.getPhoneNo())
                        .setEmailId(user.getEmailId())
                        .setPassword(user.getPassword())
                        .setDisplayId(user.getDisplayId())
                        .setIsActive(user.isActive())
                        .setIsBlocked(user.isBlocked())
                        .setTotalRatings(user.getTotalRatings())
                        .setRatingCount(user.getRatingCount())
                        .setCreatedAt(user.getCreatedAt())
                        .setUpdatedAt(user.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void updateUser(UserDTO userDTO, int userId) throws ApplicationException {
        if (userId == 0) {
            throw new ApplicationException("User ID is required");
        }
        Optional<User> userOpt = userDAO.getUser(userId);
        if (!userOpt.isPresent()) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        User existingUser = userOpt.get();
        User updatedUser = new User.UserBuilder()
                .setUserId(existingUser.getUserId())
                .setFirstName(userDTO.getFirstName() != null ? userDTO.getFirstName() : existingUser.getFirstName())
                .setLastName(userDTO.getLastName() != null ? userDTO.getLastName() : existingUser.getLastName())
                .setPhoneNo(userDTO.getPhoneNo() != null ? userDTO.getPhoneNo() : existingUser.getPhoneNo())
                .setEmailId(userDTO.getEmailId() != null ? userDTO.getEmailId() : existingUser.getEmailId())
                .build();
        userDAO.updateUser(updatedUser);
    }

    public void updatePassword(String email, String oldPassword, String newPassword) throws ApplicationException {
        Optional<User> userOpt = userDAO.getUserByEmail(email);
        if (!userOpt.isPresent()) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(oldPassword)) {
            throw new ApplicationException(Message.User.PASSWORD_MISMATCHED);
        }
        userDAO.updatePassword(email, newPassword);
    }

    public void blockUser(int userId) throws ApplicationException {
        if (!userDAO.isUserExist(userId)) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        userDAO.blockUser(userId);
    }

    public String getUserNameById(int userId) throws ApplicationException {
        return userDAO.getUserFullName(userId);
    }

    public String getUserDisplayIdById(int userId) throws ApplicationException {
        return userDAO.getUserDisplayId(userId);
    }

    public String getUserFullNameById(int userId) throws ApplicationException {
        return userDAO.getUserFullName(userId);
    }

    public Optional<User> getUserDetails(int fromUserId) throws ApplicationException {
        return userDAO.getUser(fromUserId);
    }

    public List<UserDTO> getUsersWithLessRatingAndReviews(int ratingThreshold, int reviewCountThreshold) throws ApplicationException {
        List<User> users = userDAO.getUsersByLowRatingAndReviewCount(ratingThreshold, reviewCountThreshold);
        return users.stream().map(user -> new UserDTO.UserDTOBuilder()
                .setUserId(user.getUserId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setDisplayId(user.getDisplayId())
                .setTotalRatings(user.getTotalRatings())
                .setRatingCount(user.getRatingCount())
                .build()
        ).collect(Collectors.toList());
    }


    public List<User> getCustomersByOffsetAndLimit(int offset, int limit) throws ApplicationException {
        return userDAO.getUsersByPagination(offset, limit);
    }

    public List<User> getCustomersByPage(int page, int recordsPerPage) throws ApplicationException {
        int offset = (page - 1) * recordsPerPage;
        int limit = recordsPerPage;
        return userDAO.getUsersByPagination(offset, limit);
    }

    public void addFavouriteUser(int customerId, int driverId) throws ApplicationException {
        if (!userDAO.isUserExist(customerId)) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        if (!userDAO.isUserExist(driverId)) {
            throw new ApplicationException(Message.User.DRIVER_NOT_FOUND);
        }
        if(userDAO.isAlreadyFavourite(customerId, driverId)){
            throw new ApplicationException(Message.User.DRIVER_ALREADY_FAVOURITE);
        }
        userDAO.addFavouriteUser(customerId, driverId);
    }

    public void removeFavouriteDriver(int customerId, int driverId) throws ApplicationException {
        userDAO.removeFavouriteUser(customerId, driverId);
    }
}
