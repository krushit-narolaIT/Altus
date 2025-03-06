package com.krushit.dao;

import com.krushit.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAOImpl implements IRoleDAO {
    private final Connection connection = DBConnection.getConnection();
    private static final String GET_USER_ROLE_ID = "SELECT role_id FROM roles WHERE role = ?";

    @Override
    public int getRoleID(String role) throws SQLException {
        int roleId = 0;

        try (PreparedStatement statement = connection.prepareStatement(GET_USER_ROLE_ID)) {
            statement.setString(1, role);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    roleId = resultSet.getInt(1);
                }
            }
        }
        return roleId;
    }
}
