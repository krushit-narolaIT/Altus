package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.dao.VehicleDAO;
import com.krushit.exception.ApplicationException;
import com.krushit.model.BrandModel;
import com.krushit.model.VehicleService;
import com.krushit.model.VehicleType;

public class VehicleRideService {
    private final VehicleDAO vehicleDAO = new VehicleDAO();

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        if(!VehicleType.isValidVehicleType(vehicleService.getVehicleType())){
            throw new ApplicationException(Message.Vehicle.INVALID_VEHICLE_TYPE);
        }
        vehicleDAO.addVehicleService(vehicleService);
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        vehicleDAO.addBrandModel(brandModel);
    }

}
