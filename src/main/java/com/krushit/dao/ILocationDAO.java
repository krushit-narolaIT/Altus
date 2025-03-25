package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.Location;

import java.sql.SQLException;
import java.util.List;

public interface ILocationDAO {
    void addLocation(String location) throws DBException;
    String getLocationNameById(int locationId) throws DBException;
    List<Location> getAllLocations() throws DBException;
    boolean deleteLocation(int locationId) throws DBException;
    double getCommissionByDistance(double distance) throws DBException;
}
