package com.krushit.service;

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

public class CustomerService {
    private final IUserDAO userDAO = new UserDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();

    public void registerUser(User user) throws SQLException, ApplicationException, ClassNotFoundException {
        userDAO.registerUser(user);
    }

    public UserDTO userLogin(String email, String password) throws ApplicationException, SQLException, ClassNotFoundException {
//        User user = userDAO.userLogin(email, password);
//        UserDTO userDTO = convertToDTO(user);
//        return userDTO;
        Mapper mapper = new Mapper();
        return mapper.convertToDTO(userDAO.userLogin(email, password));
    }

    public List<User> getAllCustomers() throws DBException {
        return userDAO.fetchAllCustomers();
    }

    public List<Driver> getAllDrivers() throws DBException {
        return driverDAO.fetchAllDrivers();
    }
}
