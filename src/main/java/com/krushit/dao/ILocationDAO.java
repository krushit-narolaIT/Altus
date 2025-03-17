package com.krushit.dao;

import com.krushit.exception.DBException;

import java.sql.SQLException;

public interface ILocationDAO {
    void addLocation(String location) throws SQLException, ClassNotFoundException, DBException;
}
