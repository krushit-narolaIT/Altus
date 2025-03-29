package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.mapper.Mapper;
import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IUserDAO;
import com.krushit.dao.UserDAOImpl;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dto.UserDTO;
import com.krushit.model.Driver;
import com.krushit.model.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final IUserDAO userDAO = new UserDAOImpl();

    public void userBlocked(int userId) throws ApplicationException {
        if(userDAO.isUserBlocked(userId)){
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
        if(!userDAO.isValidUser(email, password)){
            throw new ApplicationException(Message.User.PLEASE_ENTER_VALID_EMAIL_OR_PASS);
        }
        Mapper mapper = Mapper.getInstance();
        return mapper.convertToDTO(userDAO.userLogin(email, password));
    }

    public List<User> getAllCustomers() throws ApplicationException {
        return userDAO.fetchAllCustomers();
    }

    public void updateUser(UserDTO userDTO, int userId) throws ApplicationException {
        if (userId == 0) {
            throw new ApplicationException("User ID is required");
        }
        Optional<User> userOpt = userDAO.getUserDetails(userId);
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
        Optional<User> userOpt = userDAO.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        User user = userOpt.get();
        if (!user.getPassword().equals(oldPassword)) {
            throw new ApplicationException(Message.User.PASSWORD_MISMATCHED);
        }
        userDAO.updatePassword(email, newPassword);
    }

    public void blockUser(int userId) throws ApplicationException{
        if(!userDAO.isUserExist(userId)){
            throw new ApplicationException(Message.User.USER_NOT_FOUND);
        }
        userDAO.blockUser(userId);
    }

    public String getUserNameById(int userId) throws ApplicationException{
        return userDAO.getUserFullNameById(userId);
    }
}
