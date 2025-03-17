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

public class VehicleRideService {
    private IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private IDriverDAO driverDAO = new DriverDAOImpl();

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
        vehicle.setDriverId(driverId);
        vehicleDAO.addVehicle(vehicle);
    }
}
