package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.exception.ApplicationException;
import com.krushit.exception.DBException;
import com.krushit.model.BrandModel;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;
import com.krushit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleDAOImpl implements IVehicleDAO{
    private final String INSERT_VEHICLE_SERVICE = "INSERT INTO Vehicle_Service (service_name, base_fare, per_km_rate, vehicle_type, max_passengers, commission_percentage) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private final String INSERT_BRAND_MODEL = "INSERT INTO Brand_Models (service_id, brand_name, model, min_year) " +
            "VALUES (?, ?, ?, ?)";
    private static final String INSERT_VEHICLE_QUERY = "INSERT INTO vehicles (driver_id, brand_model_id, registration_number, year, fuel_type,transmission, ground_clearance, wheel_base, verification_status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String IS_DRIVER_VEHICLE_EXIST = "SELECT 1 FROM vehicles WHERE driver_id = ?";
    private static final String CHECK_VEHICLE_SERVICE_EXIST = "SELECT 1 FROM Vehicle_Service WHERE LOWER(service_name) = ?";

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
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BRAND_MODEL)) {
            preparedStatement.setInt(1, brandModel.getServiceId());
            preparedStatement.setString(2, brandModel.getBrandName());
            preparedStatement.setString(3, brandModel.getModel());
            preparedStatement.setInt(4, brandModel.getMinYear());
            preparedStatement.executeUpdate();
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
}
