package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.exception.DBException;
import com.krushit.dto.BrandModelResponseDTO;
import com.krushit.model.BrandModel;
import com.krushit.model.RideRequest;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;

import java.sql.Date;
import java.sql.*;
import java.util.*;

public class VehicleDAOImpl implements IVehicleDAO {
    private static final String INSERT_VEHICLE_SERVICE = "INSERT INTO Vehicle_Service (service_name, base_fare, per_km_rate, vehicle_type, max_passengers, commission_percentage) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String INSERT_BRAND_MODEL = "INSERT INTO Brand_Models (service_id, brand_name, model, min_year) " +
            "VALUES (?, ?, ?, ?)";
    private static final String CHECK_BRAND_MODEL_EXISTENCE_QUERY = "SELECT COUNT(*) FROM Brand_Models WHERE brand_name = ? AND model = ?";
    private static final String CHECK_BRAND_MODEL_EXISTENCE_BY_ID = "SELECT COUNT(*) FROM Brand_Models WHERE brand_model_id = ?";
    private static final String INSERT_VEHICLE_QUERY = "INSERT INTO vehicles (driver_id, brand_model_id, registration_number, year, fuel_type,transmission, ground_clearance, wheel_base, verification_status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String IS_DRIVER_VEHICLE_EXIST = "SELECT 1 FROM vehicles WHERE driver_id = ?";
    private static final String CHECK_VEHICLE_SERVICE_EXIST = "SELECT 1 FROM Vehicle_Service WHERE LOWER(service_name) = ?";
    private static final String GET_ALL_BRAND_MODELS = "SELECT brand_model_id, brand_name, model FROM brand_models";
    private static final String GET_MINIMUM_VEHICLE_YEAR = "SELECT min_year FROM brand_models WHERE brand_model_id = ?";
    private static final String GET_AVAILABLE_SERVICES =
            "SELECT vs.service_id, vs.service_name, vs.base_fare, vs.per_km_rate, vs.vehicle_type, vs.max_passengers " +
                    "FROM Vehicle_Service vs " +
                    "JOIN Brand_Models bm ON vs.service_id = bm.service_id " +
                    "JOIN Vehicles v ON bm.brand_model_id = v.brand_model_id " +
                    "JOIN Drivers d ON v.driver_id = d.driver_id " +
                    "WHERE d.is_available = TRUE";
    private static final String REQUEST_FOR_A_RIDE = "INSERT INTO ride_requests (ride_request_status, pick_up_location_id, drop_off_location_id, " +
            "vehicle_service_id, user_id, ride_date, pick_up_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_DRIVER_VEHICLE = "DELETE FROM Vehicles WHERE driver_id = (SELECT driver_id FROM Drivers WHERE user_id = ?)";
    private static final String GET_SERVICE_BY_SERVICE_ID = "SELECT * FROM Vehicle_Service WHERE service_id = ?";

    @Override
    public boolean isVehicleServiceExists(String serviceName) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement statement = connection.prepareStatement(CHECK_VEHICLE_SERVICE_EXIST)) {
            statement.setString(1, serviceName.toLowerCase());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_SERVICE + " " + e.getMessage(), e);
        }
    }

    @Override
    public void addVehicleService(VehicleService vehicleService) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement insertStmt = connection.prepareStatement(INSERT_VEHICLE_SERVICE)) {
            insertStmt.setString(1, vehicleService.getServiceName());
            insertStmt.setDouble(2, vehicleService.getBaseFare());
            insertStmt.setDouble(3, vehicleService.getPerKmRate());
            insertStmt.setString(4, vehicleService.getVehicleType());
            insertStmt.setInt(5, vehicleService.getMaxPassengers());
            insertStmt.setDouble(6, vehicleService.getCommissionPercentage());
            insertStmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_SERVICE + " " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isBrandModelExists(String brandName, String model) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(CHECK_BRAND_MODEL_EXISTENCE_QUERY)) {
            checkStatement.setString(1, brandName);
            checkStatement.setString(2, model);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_MODEL + " " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isBrandModelExistsByID(int brandModelId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CHECK_BRAND_MODEL_EXISTENCE_BY_ID)) {
            stmt.setInt(1, brandModelId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_BRAND_MODEL, e);
        }
    }

    @Override
    public void addBrandModel(BrandModel brandModel) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(INSERT_BRAND_MODEL)) {
            insertStatement.setInt(1, brandModel.getServiceId());
            insertStatement.setString(2, brandModel.getBrandName());
            insertStatement.setString(3, brandModel.getModel());
            insertStatement.setInt(4, brandModel.getMinYear());
            insertStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_MODEL + " " + e.getMessage(), e);
        }
    }

    @Override
    public void addVehicle(Vehicle vehicle) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_VEHICLE_QUERY)) {
            stmt.setInt(1, vehicle.getDriverId());
            stmt.setInt(2, vehicle.getBrandModelId());
            stmt.setString(3, vehicle.getRegistrationNumber());
            stmt.setInt(4, vehicle.getYear());
            stmt.setString(5, vehicle.getFuelType());
            stmt.setString(6, vehicle.getTransmission());
            stmt.setDouble(7, vehicle.getGroundClearance());
            stmt.setDouble(8, vehicle.getWheelBase());
            stmt.setString(9, Message.Vehicle.PENDING);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_ADDING_VEHICLE + " " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isDriverVehicleExist(int driverID) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DRIVER_VEHICLE_EXIST)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECK_VEHICLE_EXISTENCE + " " + e.getMessage(), e);
        }
    }

    @Override
    public List<BrandModelResponseDTO> getAllBrandModels() throws DBException {
        List<BrandModelResponseDTO> brandModelList = new ArrayList<>();
        Map<String, List<String>> brandModelMap = new HashMap<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BRAND_MODELS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String brandName = resultSet.getString("brand_name");
                String model = resultSet.getString("model");
                brandModelMap.computeIfAbsent(brandName, k -> new ArrayList<>()).add(model);
            }
            for (Map.Entry<String, List<String>> entry : brandModelMap.entrySet()) {
                brandModelList.add(new BrandModelResponseDTO(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_GETTING_ALL_BRAND_MODELS, e);
        }
        return brandModelList;
    }

    @Override
    public int getMinYearForBrandModel(int brandModelId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_MINIMUM_VEHICLE_YEAR)) {
            stmt.setInt(1, brandModelId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("min_year");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_MIN_YEAR, e);
        }
        return 0;
    }

    @Override
    public List<VehicleService> getAllAvailableVehicleServices() throws DBException {
        List<VehicleService> services = new ArrayList<>();
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_AVAILABLE_SERVICES);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VehicleService service = new VehicleService(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("base_fare"),
                        rs.getDouble("per_km_rate"),
                        rs.getString("vehicle_type"),
                        rs.getInt("max_passengers")
                );
                services.add(service);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_BRAND_MODEL, e);
        }
        return services;
    }

    public void bookRide(RideRequest rideRequest) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REQUEST_FOR_A_RIDE)) {
            preparedStatement.setString(1, RideRequestStatus.PENDING.getStatus());
            preparedStatement.setInt(2, rideRequest.getPickUpLocationId());
            preparedStatement.setInt(3, rideRequest.getDropOffLocationId());
            preparedStatement.setInt(4, rideRequest.getVehicleServiceId());
            preparedStatement.setInt(5, rideRequest.getUserId());
            preparedStatement.setDate(6, Date.valueOf(rideRequest.getRideDate()));
            preparedStatement.setTime(7, Time.valueOf(rideRequest.getPickUpTime()));
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_BRAND_MODEL, e);
        }
    }

    @Override
    public Optional<VehicleService> getVehicleService(int serviceId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = connection.prepareStatement(GET_SERVICE_BY_SERVICE_ID)) {
            ps.setInt(1, serviceId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(new VehicleService(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getDouble("base_fare"),
                        rs.getDouble("per_km_rate"),
                        rs.getString("vehicle_type"),
                        rs.getInt("max_passengers"),
                        rs.getDouble("commission_percentage")
                ));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_CHECKING_BRAND_MODEL, e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteVehicleByUserId(int userId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_DRIVER_VEHICLE)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Vehicle.ERROR_OCCUR_WHILE_DELETING_VEHICLE, e);
        }
    }
}