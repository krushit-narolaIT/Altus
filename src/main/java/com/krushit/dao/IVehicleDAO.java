package com.krushit.dao;

import com.krushit.common.exception.DBException;
import com.krushit.dto.BrandModelResponseDTO;
import com.krushit.entity.RideRequest;
import com.krushit.entity.Vehicle;
import com.krushit.entity.VehicleService;
import com.krushit.entity.BrandModel;

import java.util.List;
import java.util.Optional;

public interface IVehicleDAO {
    boolean isVehicleServiceExists(String serviceName) throws DBException;
    void addVehicle(Vehicle vehicle) throws DBException;
    void addVehicleService(VehicleService vehicleService) throws DBException;
    boolean isBrandModelExists(String brandName, String model) throws DBException;
    boolean isBrandModelExistsByID(int brandModelId) throws DBException;
    void addBrandModel(BrandModel brandModel) throws DBException;
    boolean isDriverVehicleExist(int driverID) throws DBException;
    List<BrandModelResponseDTO> getAllBrandModels() throws DBException;
    int getMinYearForBrandModel(int brandModelId) throws DBException;
    List<VehicleService> getAllAvailableVehicleServices() throws DBException;
    void requestForRide(RideRequest rideRequest) throws DBException;
    Optional<VehicleService> getVehicleService(int vehicleServiceId) throws DBException;
    void deleteVehicleByUserId(int userId) throws DBException;
}
