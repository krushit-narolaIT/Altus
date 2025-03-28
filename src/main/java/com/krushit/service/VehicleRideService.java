package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.enums.RideStatus;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dao.*;
import com.krushit.dto.RideCancellationDetails;
import com.krushit.dto.RideDTO;
import com.krushit.dto.RideResponseDTO;
import com.krushit.dto.RideServiceDTO;
import com.krushit.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VehicleRideService {
    private final IVehicleDAO vehicleDAO = new VehicleDAOImpl();
    private final IDriverDAO driverDAO = new DriverDAOImpl();
    private final IUserDAO userDAO = new UserDAOImpl();
    private final ILocationDAO locationDAO = new LocationDAOImpl();
    private final LocationService locationService = new LocationService();
    private final IRideDAO rideDAO = new RideDAOImpl();

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

    public List<RideResponseDTO> getAllRequest(int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        List<RideRequest> rideRequests = rideDAO.getAllMatchingRideRequests(driverId);
        List<RideResponseDTO> responseList = new ArrayList<>();
        for (RideRequest rideRequest : rideRequests) {
            try {
                String pickUpLocation = locationDAO.getLocationNameById(rideRequest.getPickUpLocationId());
                String dropOffLocation = locationDAO.getLocationNameById(rideRequest.getDropOffLocationId());
                String userDisplayId = userDAO.getUserDisplayIdById(rideRequest.getUserId());
                String userFullName = userDAO.getUserFullNameById(rideRequest.getUserId());
                RideResponseDTO responseDTO = new RideResponseDTO.RideResponseDTOBuilder()
                        .setRideRequestId(rideRequest.getRideRequestId())
                        .setPickUpLocation(pickUpLocation)
                        .setDropOffLocation(dropOffLocation)
                        .setRideDate(rideRequest.getRideDate())
                        .setPickUpTime(rideRequest.getPickUpTime())
                        .setUserDisplayId(userDisplayId)
                        .setUserFullName(userFullName)
                        .build();
                responseList.add(responseDTO);
            } catch (DBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }
        return responseList;
    }

    /*public List<RideResponseDTO> getAllRequest(int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        List<RideRequest> rideRequests = rideDAO.getAllMatchingRideRequests(driverId);
        return rideRequests.stream()
                .map(rideRequest -> {
                    try {
                        String pickUpLocation = locationDAO.getLocationNameById(rideRequest.getPickUpLocationId());
                        String dropOffLocation = locationDAO.getLocationNameById(rideRequest.getDropOffLocationId());
                        String userDisplayId = userDAO.getUserDisplayIdById(rideRequest.getUserId());

                        return new RideResponseDTO.RideResponseDTOBuilder()
                                .setRideRequestId(rideRequest.getRideRequestId())
                                .setPickUpLocation(pickUpLocation)
                                .setDropOffLocation(dropOffLocation)
                                .setRideDate(rideRequest.getRideDate())
                                .setPickUpTime(rideRequest.getPickUpTime())
                                .setUserDisplayId(userDisplayId)
                                .build();
                    } catch (DBException e) {
                        throw new DBException("Error fetching ride details", e);
                    }
                })
                .collect(Collectors.toList());
    }*/

    public void acceptRide(int driverId, int rideRequestId) throws Exception {
        Optional<RideRequest> rideRequestOpt = rideDAO.getRideRequestById(rideRequestId);
        if(!rideRequestOpt.isPresent()){
            throw new ApplicationException(Message.Ride.RIDE_REQUEST_NOT_EXIST);
        }
        RideRequest rideRequest = rideRequestOpt.get();
        if (rideRequest == null || !rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_LOCATION);
        }
        String userIdPart = String.format("%04d", rideRequest.getUserId() % 10000);
        String driverIdPart = String.format("%04d", driverId % 10000);
        String displayId = "R" + userIdPart + "I" + driverIdPart;
        Optional<VehicleService> vehicleService =  vehicleDAO.getServiceById(rideRequest.getVehicleServiceId());
        if(!vehicleService.isPresent()){
            throw new ApplicationException(Message.Vehicle.VEHICLE_SERVICE_NOT_EXIST);
        }
        VehicleService service = vehicleService.get();
        double distance = locationService.calculateDistance(rideRequest.getPickUpLocationId(), rideRequest.getDropOffLocationId());
        double commissionPercentage = locationDAO.getCommissionByDistance(distance);
        double totalCost = service.getBaseFare() + (service.getPerKmRate() * distance);
        double systemEarning = (totalCost * commissionPercentage) / 100;
        double driverEarning = totalCost - systemEarning;
        Ride ride = new Ride.RideBuilder()
                .setRideStatus(RideStatus.SCHEDULED)
                .setPickLocationId(rideRequest.getPickUpLocationId())
                .setDropOffLocationId(rideRequest.getDropOffLocationId())
                .setCustomerId(rideRequest.getUserId())
                .setDriverId(driverId)
                .setRideDate(rideRequest.getRideDate())
                .setPickUpTime(rideRequest.getPickUpTime())
                .setDisplayId(displayId)
                .setTotalKm(distance)
                .setTotalCost(totalCost)
                .setPaymentMode(PaymentMode.CASH)
                .setPaymentStatus(PaymentStatus.PENDING)
                .setCommissionPercentage(commissionPercentage)
                .setDriverEarning(driverEarning)
                .setSystemEarning(systemEarning)
                .build();
        rideDAO.createRide(ride);
    }

    public void cancelRide(int rideId, int userId, boolean isDriver) throws ApplicationException {
        Optional<Ride> rideOpt = rideDAO.getRideById(rideId);
        if (!rideOpt.isPresent()) {
            throw new ApplicationException(Message.Ride.RIDE_NOT_FOUND_FOR_CANCELLATION);
        }
        Ride ride = rideOpt.get();
        if (!isDriver && ride.getCustomerId() != userId) {
            throw new ApplicationException(Message.Ride.RIDE_NOT_BELONG_TO_THIS_CUSTOMER);
        }
        if (isDriver && ride.getDriverId() != userId) {
            throw new ApplicationException(Message.Ride.RIDE_NOT_BELONG_TO_THIS_DRIVER);
        }
        if (ride.getRideStatus() == RideStatus.CANCELLED || ride.getRideStatus() == RideStatus.COMPLETED) {
            throw new ApplicationException(Message.Ride.RIDE_CANNOT_BE_CANCELLED + ride.getRideStatus().getStatus());
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pickupDateTime = LocalDateTime.of(ride.getRideDate(), ride.getPickUpTime());
        long minutesLeft = Duration.between(now, pickupDateTime).toMinutes();
        double cancellationCharge = 0.0;
        double driverEarning = 0.0;
        double systemEarning = 0.0;
        double driverPenalty = 0.0;
        if (isDriver) {
            if (now.isAfter(pickupDateTime)) {
                throw new ApplicationException(Message.Ride.RIDE_ALREADY_STARTED_CANNOT_CANCEL);
            }
            driverPenalty = 120.0;
        } else {
            if (minutesLeft < 40) {
                cancellationCharge = ride.getTotalCost() * 0.11;
                driverEarning = cancellationCharge * 0.45;
                systemEarning = cancellationCharge * 0.55;
            }
        }
        String rideStatus = null;
        if (isDriver) {
            rideStatus = RideStatus.REJECTED.getStatus();
        } else {
            rideStatus = RideStatus.CANCELLED.getStatus();
        }
        RideCancellationDetails cancellationDetails = new RideCancellationDetails.RideCancellationDetailsBuilder()
                .setRideId(rideId)
                .setRideStatus(rideStatus)
                .setCancellationCharge(cancellationCharge)
                .setDriverEarning(driverEarning)
                .setSystemEarning(systemEarning)
                .setDriverPenalty(driverPenalty)
                .build();
        rideDAO.updateRideCancellation(cancellationDetails);
    }

    public List<RideDTO> getAllRides(int userId, boolean isDriver) throws DBException {
        List<Ride> rideList = rideDAO.getAllRideByUserId(userId);
        List<RideDTO> rideDTOList = new ArrayList<>();
        for (Ride ride : rideList) {
            try {
                String customerName = userDAO.getUserFullNameById(ride.getCustomerId());
                String driverName = userDAO.getUserFullNameById(ride.getDriverId());
                RideDTO.RideDTOBuilder builder = new RideDTO.RideDTOBuilder()
                        .setRideId(ride.getRideId())
                        .setRideStatus(ride.getRideStatus())
                        .setPickLocationId(locationDAO.getLocationNameById(ride.getPickLocationId()))
                        .setDropOffLocationId(locationDAO.getLocationNameById(ride.getDropOffLocationId()))
                        .setCustomerName(customerName)
                        .setDriverName(driverName)
                        .setRideDate(ride.getRideDate())
                        .setPickUpTime(ride.getPickUpTime())
                        .setDropOffTime(ride.getDropOffTime())
                        .setDisplayId(ride.getDisplayId())
                        .setTotalKm(ride.getTotalKm())
                        .setTotalCost(ride.getTotalCost())
                        .setPaymentMode(ride.getPaymentMode())
                        .setPaymentStatus(ride.getPaymentStatus());
                if (isDriver) {
                    builder.setDriverEarning(ride.getDriverEarning())
                            .setCancellationDriverEarning(ride.getCancellationDriverEarning())
                            .setDriverPenalty(ride.getDriverPenalty());
                } else {
                    builder.setCancellationCharge(ride.getCancellationCharge());
                }
                rideDTOList.add(builder.build());
            } catch (DBException e) {
                throw new DBException(e.getMessage(), e);
            }
        }
        return rideDTOList;
    }

    public void deleteVehicle(int userId) throws ApplicationException {
        int driverId = driverDAO.getDriverIdFromUserId(userId);
        if(!vehicleDAO.isDriverVehicleExist(driverId)){
            throw new ApplicationException(Message.Vehicle.VEHICLE_NOT_EXIST);
        }
        vehicleDAO.deleteVehicleByUserId(userId);
    }

    public String fetchRideStatus(int rideId) throws DBException {
        return rideDAO.getRideStatus(rideId);
    }
}
