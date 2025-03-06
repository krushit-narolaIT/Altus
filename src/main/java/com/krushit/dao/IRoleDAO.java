package com.krushit.dao;

import java.sql.SQLException;

public interface IRoleDAO {
    int getRoleID(String role) throws SQLException;
}
