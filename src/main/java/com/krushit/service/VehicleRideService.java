package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.dao.*;
import com.krushit.dto.RideRequestDTO;
import com.krushit.dto.RideServiceDTO;
import com.krushit.model.BrandModel;
import com.krushit.model.RideRequest;
import com.krushit.model.Vehicle;
import com.krushit.model.VehicleService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleRideService {
    private final IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();
    private final ILocationDAO locationDAO = new LocationDAOImpl();
    private final LocationService locationService = new LocationService();

    public void addVehicleService(VehicleService vehicleService) throws ApplicationException {
        if (vehicleDAO.isVehicleServiceExists(vehicleService.getServiceName())) {
            throw new ApplicationException(Message.Vehicle.VEHICLE_SERVICE_ALREADY_EXISTS);
        }
        vehicleDAO.addVehicleService(vehicleService);
    }

    public void addBrandModel(BrandModel brandModel) throws ApplicationException {
        if (vehicleDAO.isBrandModelExists(brandModel.getBrandName(), brandModel.getModel())) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_ALREADY_EXISTS);
        }
        vehicleDAO.addBrandModel(brandModel);
    }

    public void addVehicle(Vehicle vehicle, int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        if (!driverDAO.isDriverDocumentUploaded(driverId)) {
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_UPLOADED);
        }
        if (!driverDAO.isDriverDocumentVerified(driverId)) {
            throw new ApplicationException(Message.Driver.DOCUMENT_NOT_VERIFIED);
        }
        if (vehicleDAO.isDriverVehicleExist(driverId)) {
            throw new ApplicationException(Message.Vehicle.DRIVER_VEHICLE_ALREADY_EXIST);
        }
        if (vehicleDAO.isBrandModelExistsByID(vehicle.getVehicleId())) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_NOT_SUPPORTED);
        }
        Integer minYear = vehicleDAO.getMinYearForBrandModel(vehicle.getBrandModelId());
        if (vehicle.getYear() < minYear) {
            throw new ApplicationException(Message.Vehicle.BRAND_MODEL_YEAR_NOT_SUPPORTED);
        }
        vehicle.setDriverId(driverId);
        driverDAO.updateDriverAvailability(driverId);
        vehicleDAO.addVehicle(vehicle);
    }

    public Map<String, List<String>> getAllBrandModels() throws ApplicationException {
        return vehicleDAO.getAllBrandModels();
    }

    public List<RideServiceDTO> getAvailableRides(int fromId, int toId) throws Exception {
        String fromLocation = locationDAO.getLocationNameById(fromId);
        String toLocation = locationDAO.getLocationNameById(toId);
        double distance = locationService.calculateDistance(fromId, toId);
        List<VehicleService> availableServices = vehicleDAO.getAllAvailableVehicleServices();
        List<RideServiceDTO> rideOptions = new ArrayList<>();
        for (VehicleService service : availableServices) {
            double totalPrice = service.getBaseFare() + (service.getPerKmRate() * distance);
            RideServiceDTO ride = new RideServiceDTO(
                    service.getServiceId(),
                    service.getServiceName(),
                    totalPrice,
                    fromLocation,
                    toLocation,
                    service.getMaxPassengers()
            );
            rideOptions.add(ride);
        }
        return rideOptions;
    }

    public void bookRide(RideRequest rideRequest) throws Exception {
        List<RideServiceDTO> rideServiceDTOS = getAvailableRides(rideRequest.getPickUpLocationId(), rideRequest.getDropOffLocationId());
        boolean isServiceAvailable = rideServiceDTOS.stream()
                .map(RideServiceDTO::getServiceId)
                .anyMatch(serviceId -> serviceId == rideRequest.getVehicleServiceId());
        if (!isServiceAvailable) {
            throw new ApplicationException(Message.Ride.REQUESTED_SERVICE_IS_NOT_AVAILABLE);
        }
        if (locationDAO.getLocationNameById(rideRequest.getPickUpLocationId()).isEmpty()
                || locationDAO.getLocationNameById(rideRequest.getDropOffLocationId()).isEmpty()) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_LOCATION);
        }
        vehicleDAO.bookRide(rideRequest);
    }
}
