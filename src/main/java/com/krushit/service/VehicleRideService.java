package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.DBException;
import com.krushit.dao.*;
import com.krushit.common.exception.ApplicationException;
import com.krushit.model.BrandModel;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;

import java.util.List;
import java.util.Map;

public class VehicleRideService {
    private final IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        vehicleDAO.addVehicleService(vehicleService);
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        vehicleDAO.addBrandModel(brandModel);
    }

    public void addVehicle(Vehicle vehicle, int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        if(!driverDAO.isDriverDocumentUploaded(driverId)){
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_UPLOADED);
        }
        if(!driverDAO.isDriverDocumentVerified(driverId)){
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_VERIFIED);
        }
        if(vehicleDAO.isDriverVehicleExist(driverId)){
            throw new ApplicationException(Message.Vehicle.DRIVER_VEHICLE_ALREADY_EXIST);
        }
        if(vehicleDAO.isBrandModelExist(vehicle.getBrandModelId())){
            throw new ApplicationException(Message.Vehicle.DRIVER_VEHICLE_ALREADY_EXIST);
        }
        int minYear = vehicleDAO.getMinYearForBrandModel(vehicle.getBrandModelId());
        if (vehicle.getYear() < minYear) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_NOT_SUPPORTED);
        }
        vehicle.setDriverId(driverId);
        vehicleDAO.addVehicle(vehicle);
    }

    public Map<String, List<String>> getAllBrandModels() throws DBException {
        return vehicleDAO.getAllBrandModels();
    }
}
