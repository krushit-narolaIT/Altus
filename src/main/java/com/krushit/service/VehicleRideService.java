package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.dao.DriverDAOImpl;
import com.krushit.dao.IDriverDAO;
import com.krushit.dao.IVehicleDAO;
import com.krushit.dao.VehicleDAOImpl;
import com.krushit.exception.ApplicationException;
import com.krushit.model.BrandModel;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;
import com.krushit.model.VehicleType;

public class VehicleRideService {
    private final IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        if(!VehicleType.isValidVehicleType(vehicleService.getVehicleType())){
            throw new ApplicationException(Message.Vehicle.INVALID_VEHICLE_TYPE);
        }
        vehicleDAO.addVehicleService(vehicleService);
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        vehicleDAO.addBrandModel(brandModel);
    }

    public void addVehicle(Vehicle vehicle) throws ApplicationException {
        //driverDAO.getDriverIdFromUserId();
        if (vehicle.getRegistrationNumber() == null || vehicle.getBrandModelId() == null || vehicle.getYear() == 0) {
            throw new ApplicationException("Invalid vehicle data. Required fields are missing.");
        }

        vehicleDAO.addVehicle(vehicle);
    }

}
