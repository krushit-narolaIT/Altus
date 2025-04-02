package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.model.Location;

import java.util.List;

public interface ILocationDAO {
    void addLocation(String location) throws DBException;
    String getLocationName(int locationId) throws DBException;
    List<Location> getAllLocations() throws DBException;
    void deleteLocation(int locationId) throws DBException;
    double getCommissionByDistance(double distance) throws DBException;
}
