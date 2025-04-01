package com.krushit.dao;

import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.exception.DBException;
import com.krushit.model.RideRequest;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;
import com.krushit.model.BrandModel;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IVehicleDAO {
    boolean isVehicleServiceExists(String serviceName) throws DBException;
    void addVehicle(Vehicle vehicle) throws DBException;
    void addVehicleService(VehicleService vehicleService) throws DBException;
    boolean isBrandModelExists(String brandName, String model) throws DBException;
    boolean isBrandModelExistsByID(int brandModelId) throws DBException;
    void addBrandModel(BrandModel brandModel) throws DBException;
    boolean isDriverVehicleExist(int driverID) throws DBException;
    Map<String, List<String>> getAllBrandModels() throws DBException;
    Integer getMinYearForBrandModel(int brandModelId) throws DBException;
    List<VehicleService> getAllAvailableVehicleServices() throws DBException;
    void bookRide(RideRequest rideRequest) throws DBException;
    Optional<VehicleService> getServiceById(int vehicleServiceId) throws DBException;
    void deleteVehicleByUserId(int userId) throws DBException;
}
