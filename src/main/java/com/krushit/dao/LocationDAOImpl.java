package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.exception.DBException;
import com.krushit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationDAOImpl implements ILocationDAO{
    private final String ADD_LOCATION = "INSERT INTO LOCATIONS (name) VALUES (?)";
    @Override
    public void addLocation(String location) throws SQLException, ClassNotFoundException, DBException {
        try(Connection connection = DBConnection.INSTANCE.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD_LOCATION)){
                statement.setString(1, location);
                statement.executeUpdate();
        } catch (SQLException e){
            throw new DBException(Message.Database.DATABASE_ERROR + " " + e.getMessage(), e);
        }
    }
}
