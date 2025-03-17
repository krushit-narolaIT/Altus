package com.krushit.service;

import com.krushit.dao.ILocationDAO;
import com.krushit.dao.LocationDAOImpl;
import com.krushit.common.exception.DBException;

import java.sql.SQLException;

public class LocationService {
    private ILocationDAO locationDAO = new LocationDAOImpl();

    public void addLocation(String location) throws DBException, SQLException, ClassNotFoundException {
        locationDAO.addLocation(location);
    }
}
