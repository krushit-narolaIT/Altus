package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.model.Location;
import com.krushit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDAOImpl implements ILocationDAO{
    private static final String GET_LOCATION = "SELECT name FROM locations WHERE location_id = ?";
    private static final String ADD_LOCATION = "INSERT INTO locations (name) VALUES (?)";
    private static final String GET_ALL_LOCATIONS = "SELECT location_id, name FROM locations";
    private static final String DELETE_LOCATION = "DELETE FROM locations WHERE location_id = ?";

    @Override
    public void addLocation(String location) throws DBException {
        try(Connection connection = DBConnection.INSTANCE.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_LOCATION)){
                statement.setString(1, location);
                statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e){
            throw new DBException(e.getMessage(), e);
        }
    }

    @Override
    public String getLocationNameById(int locationId) throws DBException {
        String locationName = null;
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement prepareStatement = conn.prepareStatement(GET_LOCATION)) {
            prepareStatement.setInt(1, locationId);
            ResultSet rs = prepareStatement.executeQuery();
            if (rs.next()) {
                locationName = rs.getString("name");
            }
        } catch (SQLException | ClassNotFoundException e){
            throw new DBException(e.getMessage(), e);
        }
        return locationName;
    }

    @Override
    public List<Location> getAllLocations() throws DBException {
        List<Location> locations = new ArrayList<>();
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_LOCATIONS);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locations.add(new Location(rs.getInt("location_id"), rs.getString("name")));
            }
        } catch (SQLException | ClassNotFoundException e){
            throw new DBException(e.getMessage(), e);
        }
        return locations;
    }

    @Override
    public boolean deleteLocation(int locationId) throws DBException {
        try (Connection conn = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_LOCATION)) {
            stmt.setInt(1, locationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e){
            throw new DBException(e.getMessage(), e);
        }
    }
}
