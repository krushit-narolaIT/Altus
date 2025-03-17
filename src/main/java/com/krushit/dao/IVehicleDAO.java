package com.krushit.dao;

import com.krushit.common.exception.ApplicationException;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;
import com.krushit.model.BrandModel;

public interface IVehicleDAO {
    void addVehicle(Vehicle vehicle) throws ApplicationException;
    void addVehicleService(VehicleService vehicleService) throws ApplicationException;
    void addBrandModel(BrandModel brandModel) throws ApplicationException;
    boolean isDriverVehicleExist(int driverID) throws ApplicationException;
}
