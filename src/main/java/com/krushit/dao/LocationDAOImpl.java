package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.exception.DBException;
import com.krushit.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements ILocationDAO {
    private static final String GET_LOCATION = "SELECT name FROM locations WHERE location_id = ?";
    private static final String ADD_LOCATION = "INSERT INTO locations (name) VALUES (?)";
    private static final String GET_ALL_LOCATIONS = "SELECT location_id, name FROM locations";
    private static final String DELETE_LOCATION = "DELETE FROM locations WHERE location_id = ?";
    private static final String GET_COMMISSION_PERCENTAGE = "SELECT commission_percentage FROM commission_percentage WHERE ? BETWEEN from_km AND to_km";

    @Override
    public void addLocation(String location) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_LOCATION)) {
            statement.setString(1, location);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Location.ERROR_WHILE_ADDING_LOCATION, e);
        }
    }

    @Override
    public String getLocationName(int locationId) throws DBException {
        String locationName = null;
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement prepareStatement = conn.prepareStatement(GET_LOCATION)) {
            prepareStatement.setInt(1, locationId);
            ResultSet rs = prepareStatement.executeQuery();
            if (rs.next()) {
                locationName = rs.getString("name");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Location.ERROR_WHILE_GETTING_LOCATION_BY_NAME, e);
        }
        return locationName;
    }

    @Override
    public List<Location> getAllLocations() throws DBException {
        List<Location> locations = new ArrayList<>();
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_LOCATIONS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locations.add(new Location(rs.getInt("location_id"), rs.getString("name")));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Location.ERROR_WHILE_GETTING_ALL_LOCATION, e);
        }
        return locations;
    }

    @Override
    public void deleteLocation(int locationId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_LOCATION)) {
            stmt.setInt(1, locationId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Location.ERROR_WHILE_DELETING_LOCATION, e);
        }
    }

    @Override
    public double getCommissionByDistance(double distance) throws DBException {
        double commissionPercentage = 0.0;
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_COMMISSION_PERCENTAGE)) {
            pstmt.setDouble(1, distance);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                commissionPercentage = rs.getDouble("commission_percentage");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Location.ERROR_WHILE_GET_COMMISSION_BY_DISTANCE, e);
        }
        return commissionPercentage;
    }
}
