package com.krushit.dao;

import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;
import com.krushit.model.BrandModel;

import java.util.List;
import java.util.Map;

public interface IVehicleDAO {
    void addVehicle(Vehicle vehicle) throws ApplicationException;
    void addVehicleService(VehicleService vehicleService) throws ApplicationException;
    void addBrandModel(BrandModel brandModel) throws ApplicationException;
    boolean isDriverVehicleExist(int driverID) throws ApplicationException;
    Map<String, List<String>> getAllBrandModels() throws DBException;
    int getMinYearForBrandModel(int brandModelId) throws ApplicationException;
    boolean isBrandModelExist(int brandModelId) throws DBException;
    List<VehicleService> getAllAvailableVehicleServices() throws DBException;
}
