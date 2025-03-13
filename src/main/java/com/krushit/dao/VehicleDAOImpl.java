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
    private static final String INSERT_VEHICLE_QUERY = "INSERT INTO vehicles (driver_id, brand_model_id, registration_number, year, fuel_type,transmission, ground_clearance, wheel_base) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String IS_DRIVER_VEHICLE_EXIST = "SELECT COUNT(*) FROM vehicles WHERE driver_id = ?";

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_VEHICLE_SERVICE)) {

            preparedStatement.setString(1, vehicleService.getServiceName());
            preparedStatement.setDouble(2, vehicleService.getBaseFare());
            preparedStatement.setDouble(3, vehicleService.getPerKmRate());
            preparedStatement.setString(4, vehicleService.getVehicleType());
            preparedStatement.setInt(5, vehicleService.getMaxPassengers());
            preparedStatement.setDouble(6, vehicleService.getCommissionPercentage());

            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_SERVICE, e);
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
            throw new ApplicationException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_MODEL, e);
        }
    }

    public void addVehicle(Vehicle vehicle) throws ApplicationException {
        System.out.println("Vehicle :: " + vehicle);
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
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new ApplicationException("Error adding vehicle to database.", e);
        }
    }

    public boolean isDriverVehicleExist(int driverID) throws ApplicationException {
        System.out.println("in isDriverVehicleExist()");
        try (Connection connection = DBConnection.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DRIVER_VEHICLE_EXIST)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECK_VEHICLE_EXISTENCE, e);
        }
        return false;
    }
}
