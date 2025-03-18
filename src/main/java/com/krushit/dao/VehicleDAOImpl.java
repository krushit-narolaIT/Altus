package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.BrandModel;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;
import com.krushit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class VehicleDAOImpl implements IVehicleDAO{
    private final String INSERT_VEHICLE_SERVICE = "INSERT INTO Vehicle_Service (service_name, base_fare, per_km_rate, vehicle_type, max_passengers, commission_percentage) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private final String INSERT_BRAND_MODEL = "INSERT INTO Brand_Models (service_id, brand_name, model, min_year) " +
            "VALUES (?, ?, ?, ?)";
    private final String CHECK_BRAND_MODEL_EXISTENCE_QUERY = "SELECT COUNT(*) FROM Brand_Models WHERE brand_name = ? AND model = ?";
    private static final String INSERT_VEHICLE_QUERY = "INSERT INTO vehicles (driver_id, brand_model_id, registration_number, year, fuel_type,transmission, ground_clearance, wheel_base, verification_status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String IS_DRIVER_VEHICLE_EXIST = "SELECT 1 FROM vehicles WHERE driver_id = ?";
    private static final String CHECK_VEHICLE_SERVICE_EXIST = "SELECT 1 FROM Vehicle_Service WHERE LOWER(service_name) = ?";
    private static final String GET_ALL_BRAND_MODELS = "SELECT brand_model_id, brand_name, model FROM brand_models";
    private static String GET_MINIMUM_VEHICLE_YEAR = "SELECT min_year FROM brand_models WHERE brand_model_id = ?";
    private static String IS_BRAND_MODEL_EXIST = "SELECT 1 FROM brand_models WHERE brand_model_id = ?";

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(CHECK_VEHICLE_SERVICE_EXIST)) {
                statement.setString(1, vehicleService.getServiceName().toLowerCase());
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        throw new ApplicationException(Message.Vehicle.VEHICLE_SERVICE_ALREADY_EXISTS);
                    }
                }
            }
            try (PreparedStatement insertStmt = connection.prepareStatement(INSERT_VEHICLE_SERVICE)) {
                insertStmt.setString(1, vehicleService.getServiceName());
                insertStmt.setDouble(2, vehicleService.getBaseFare());
                insertStmt.setDouble(3, vehicleService.getPerKmRate());
                insertStmt.setString(4, vehicleService.getVehicleType());
                insertStmt.setInt(5, vehicleService.getMaxPassengers());
                insertStmt.setDouble(6, vehicleService.getCommissionPercentage());
                insertStmt.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_SERVICE + " " + e.getMessage(), e);
        }
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(CHECK_BRAND_MODEL_EXISTENCE_QUERY)) {
            checkStatement.setString(1, brandModel.getBrandName());
            checkStatement.setString(2, brandModel.getModel());
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new ApplicationException(Message.Vehicle.BRAND_MODEL_ALREADY_EXISTS);
                }
            }
            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_BRAND_MODEL)) {
                insertStatement.setInt(1, brandModel.getServiceId());
                insertStatement.setString(2, brandModel.getBrandName());
                insertStatement.setString(3, brandModel.getModel());
                insertStatement.setInt(4, brandModel.getMinYear());
                insertStatement.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_MODEL + " " + e.getMessage(), e);
        }
    }

    public void addVehicle(Vehicle vehicle) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_VEHICLE_QUERY)) {
            stmt.setInt(1, vehicle.getDriverId());
            stmt.setInt(2, vehicle.getBrandModelId());
            stmt.setString(3, vehicle.getRegistrationNumber());
            stmt.setInt(4, vehicle.getYear());
            stmt.setString(5, vehicle.getFuelType());
            stmt.setString(6, vehicle.getTransmission());
            stmt.setDouble(7, vehicle.getGroundClearance());
            stmt.setDouble(8, vehicle.getWheelBase());
            stmt.setString(9, Message.Vehicle.VERIFICATION_PENDING);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_VEHICLE + " " + e.getMessage(), e);
        }
    }

    public boolean isDriverVehicleExist(int driverID) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DRIVER_VEHICLE_EXIST)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECK_VEHICLE_EXISTENCE + " " + e.getMessage() , e);
        }
    }

    public Map<String, List<String>> getAllBrandModels() throws DBException {
        Map<String, List<String>> brandModelMap = new HashMap<>();
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BRAND_MODELS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int brandModelId = resultSet.getInt("brand_model_id");
                String brandName = resultSet.getString("brand_name");
                String model = resultSet.getString("model");
                brandModelMap.computeIfAbsent(brandName, k -> new ArrayList<>()).add(model);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException("Error fetching brand models: " + e.getMessage(), e);
        }
        return brandModelMap;
    }

    public int getMinYearForBrandModel(int brandModelId) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_MINIMUM_VEHICLE_YEAR)) {
            stmt.setInt(1, brandModelId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("min_year");
                } else {
                    throw new ApplicationException(Message.Vehicle.BRAND_MODEL_NOT_SUPPORTED);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_MIN_YEAR, e);
        }
    }

    public boolean isBrandModelExist(int brandModelId) throws DBException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_BRAND_MODEL_EXIST)) {
            stmt.setInt(1, brandModelId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_BRAND_MODEL, e);
        }
    }
}
