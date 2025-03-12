package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.exception.ApplicationException;
import com.krushit.model.BrandModel;
import com.krushit.model.VehicleService;
import com.krushit.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VehicleDAO {
    private final String INSERT_VEHICLE_SERVICE = "INSERT INTO Vehicle_Service (service_name, base_fare, per_km_rate, vehicle_type, max_passengers, commission_percentage) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private final String INSERT_BRAND_MODEL = "INSERT INTO Brand_Models (service_id, brand_name, model, min_year) " +
            "VALUES (?, ?, ?, ?)";


    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_VEHICLE_SERVICE)) {

            preparedStatement.setString(1, vehicleService.getServiceName());
            preparedStatement.setDouble(2, vehicleService.getBaseFare());
            preparedStatement.setDouble(3, vehicleService.getPerKmRate());
            preparedStatement.setString(4, vehicleService.getVehicleType());
            preparedStatement.setInt(5, vehicleService.getMaxPassengers());
            preparedStatement.setDouble(6, vehicleService.getCommissionPercentage());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_SERVICE, e);
        }
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_BRAND_MODEL)) {
            preparedStatement.setInt(1, brandModel.getServiceId());
            preparedStatement.setString(2, brandModel.getBrandName());
            preparedStatement.setString(3, brandModel.getModel());
            preparedStatement.setInt(4, brandModel.getMinYear());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ApplicationException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_MODEL, e);
        }
    }
}
