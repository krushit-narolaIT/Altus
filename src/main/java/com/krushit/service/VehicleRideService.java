package com.krushit.service;

import com.krushit.common.Message;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
import com.krushit.common.enums.RideRequestStatus;
import com.krushit.common.enums.RideStatus;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.DBException;
import com.krushit.dao.IRideDAO;
import com.krushit.dao.IVehicleDAO;
import com.krushit.dao.RideDAOImpl;
import com.krushit.dao.VehicleDAOImpl;
import com.krushit.dto.*;
import com.krushit.entity.BrandModel;
import com.krushit.entity.Ride;
import com.krushit.entity.RideRequest;
import com.krushit.entity.VehicleService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleRideService {
    private final DriverService driverService = new DriverService();
    private final UserService userService = new UserService();
    private final LocationService locationService = new LocationService();
    private final IVehicleDAO vehicleDAO = new VehicleDAOImpl();
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

    public List<BrandModelResponseDTO> getAllBrandModels() throws ApplicationException {
        return vehicleDAO.getAllBrandModels();
    }

    public List<RideServiceDTO> getAvailableRides(int fromId, int toId) throws Exception {
        String fromLocation = locationService.getLocationNameById(fromId);
        String toLocation = locationService.getLocationNameById(toId);
        double distance = locationService.calculateDistance(fromId, toId);
        List<VehicleService> availableServices = vehicleDAO.getAllAvailableVehicleServices();
        List<RideServiceDTO> rideOptions = new ArrayList<>();
        for (VehicleService service : availableServices) {
            BigDecimal baseFare = BigDecimal.valueOf(service.getBaseFare());
            BigDecimal perKmRate = BigDecimal.valueOf(service.getPerKmRate());
            BigDecimal km = BigDecimal.valueOf(distance);
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
        List<RideServiceDTO> rideServiceDTOS = getAvailableRides(rideRequest.getPickUpLocation().getId(), rideRequest.getDropOffLocation().getId());
        boolean isServiceAvailable = rideServiceDTOS.stream()
                .map(RideServiceDTO::getServiceId)
                .anyMatch(serviceId -> serviceId == rideRequest.getVehicleService().getServiceId());
        if (!isServiceAvailable) {
            throw new ApplicationException(Message.Ride.REQUESTED_SERVICE_IS_NOT_AVAILABLE);
        }
        if (locationService.getLocationNameById(rideRequest.getPickUpLocation().getId()).isEmpty()
                || locationService.getLocationNameById(rideRequest.getDropOffLocation().getId()).isEmpty()) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_LOCATION);
        }
        vehicleDAO.bookRide(rideRequest);
    }

    public List<RideResponseDTO> getAllRequest(int userId) throws ApplicationException {
        int driverId = driverService.getDriverIdFromUserId(userId);
        List<RideRequest> rideRequests = rideDAO.getAllMatchingRideRequests(driverId);
        List<RideResponseDTO> responseList = new ArrayList<>();
        for (RideRequest rideRequest : rideRequests) {
            try {
                String pickUpLocation = locationService.getLocationNameById(rideRequest.getPickUpLocation().getId());
                String dropOffLocation = locationService.getLocationNameById(rideRequest.getDropOffLocation().getId());
                String userDisplayId = userService.getUserDisplayIdById(rideRequest.getUser().getUserId());
                String userFullName = userService.getUserFullNameById(rideRequest.getUser().getUserId());
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

    public void acceptRide(int driverId, int rideRequestId) throws Exception {
        Optional<RideRequest> rideRequestOpt = rideDAO.getRideRequest(rideRequestId);
        if (!rideRequestOpt.isPresent()) {
            throw new ApplicationException(Message.Ride.RIDE_REQUEST_NOT_EXIST);
        }
        RideRequest rideRequest = rideRequestOpt.get();
        if (rideRequest == null || !rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_LOCATION);
        }
        Optional<Ride> conflictingRideOpt = rideDAO.getConflictingRide(driverId, rideRequest.getRideDate(), rideRequest.getPickUpTime());
        if (conflictingRideOpt.isPresent()) {
            Ride conflictingRide = conflictingRideOpt.get();
            throw new ApplicationException(Message.Ride.RIDE_ALREADY_SCHEDULED + " (Ride ID #" + conflictingRide.getDisplayId() +
                    ") at " + conflictingRide.getPickUpTime() +
                    ". " + Message.Ride.PLEASE_MANAGE_YOUR_SCHEDULE_ACCORDINGLY);
        }
        String userIdPart = String.format("%04d", rideRequest.getUser().getUserId() % 10000);
        String driverIdPart = String.format("%04d", driverId % 10000);
        String displayId = "R" + userIdPart + "I" + driverIdPart;
        Optional<VehicleService> vehicleService = vehicleDAO.getVehicleService(rideRequest.getVehicleService().getServiceId());
        VehicleService service = vehicleService.orElseThrow(() -> new ApplicationException(Message.Vehicle.VEHICLE_SERVICE_NOT_EXIST));
        double distance = locationService.calculateDistance(rideRequest.getPickUpLocation().getId(), rideRequest.getDropOffLocation().getId());
        BigDecimal commissionPercentage = locationService.getCommissionByDistance(distance);
        BigDecimal totalCost = BigDecimal.valueOf(service.getBaseFare() + (service.getPerKmRate() * distance));
        BigDecimal systemEarning = totalCost
                .multiply(commissionPercentage)
                .divide(BigDecimal.valueOf(100), 2);
        BigDecimal driverEarning = totalCost.subtract(systemEarning);
        Ride ride = new Ride.RideBuilder()
                .setRideStatus(RideStatus.SCHEDULED)
                .setPickLocation(rideRequest.getPickUpLocation())
                .setDropOffLocation(rideRequest.getDropOffLocation())
                .setCustomer(rideRequest.getUser())
                //.setDriver(driverId) TODO :: changes here
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
        rideDAO.createRide(rideRequestId, ride);
    }

    public void cancelRide(int rideId, int userId, boolean isDriver) throws ApplicationException {
        Optional<Ride> rideOpt = rideDAO.getRide(rideId);
        if (!rideOpt.isPresent()) {
            throw new ApplicationException(Message.Ride.RIDE_NOT_FOUND_FOR_CANCELLATION);
        }
        Ride ride = rideOpt.get();
        if (!isDriver && ride.getCustomer().getUserId() != userId) {
            throw new ApplicationException(Message.Ride.RIDE_NOT_BELONG_TO_THIS_CUSTOMER);
        }
        if (isDriver && ride.getDriver().getUserId() != userId) {
            throw new ApplicationException(Message.Ride.RIDE_NOT_BELONG_TO_THIS_DRIVER);
        }
        if (ride.getRideStatus() == RideStatus.CANCELLED || ride.getRideStatus() == RideStatus.COMPLETED) {
            throw new ApplicationException(Message.Ride.RIDE_CANNOT_BE_CANCELLED + ride.getRideStatus().getStatus());
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime pickupDateTime = LocalDateTime.of(ride.getRideDate(), ride.getPickUpTime());
        long minutesLeft = Duration.between(now, pickupDateTime).toMinutes();
        BigDecimal cancellationCharge = new BigDecimal("0.0");
        BigDecimal driverEarning = new BigDecimal("0.0");
        BigDecimal systemEarning = new BigDecimal("0.0");
        BigDecimal driverPenalty = new BigDecimal("0.0");
        if (isDriver) {
            if (now.isAfter(pickupDateTime)) {
                throw new ApplicationException(Message.Ride.RIDE_ALREADY_STARTED_CANNOT_CANCEL);
            }
            driverPenalty = new BigDecimal(120.0);
        } else {
            if (minutesLeft < 40) {
                cancellationCharge = ride.getTotalCost()
                        .multiply(BigDecimal.valueOf(0.11))
                        .setScale(2);
                driverEarning = cancellationCharge
                        .multiply(BigDecimal.valueOf(0.45))
                        .setScale(2);
                systemEarning = cancellationCharge
                        .multiply(BigDecimal.valueOf(0.55))
                        .setScale(2);
            }


        }
        RideStatus rideStatus = null;
        if (isDriver) {
            rideStatus = RideStatus.REJECTED;
        } else {
            rideStatus = RideStatus.CANCELLED;
        }
        RideCancellationDetailsDTO cancellationDetails = new RideCancellationDetailsDTO.RideCancellationDetailsBuilder()
                .setRideId(rideId)
                .setRideStatus(rideStatus)
                .setCancellationCharge(cancellationCharge.floatValue())
                .setDriverEarning(driverEarning.floatValue())
                .setSystemEarning(systemEarning.floatValue())
                .setDriverPenalty(driverPenalty.floatValue())
                .build();
        rideDAO.updateRideCancellation(cancellationDetails);
    }

    public List<RideDTO> getAllRides(int userId, boolean isDriver) throws ApplicationException {
        List<Ride> rideList = rideDAO.getAllRidesByUserId(userId);
        List<RideDTO> rideDTOList = new ArrayList<>();
        for (Ride ride : rideList) {
            try {
                String customerName = userService.getUserFullNameById(ride.getCustomer().getUserId());
                String driverName = userService.getUserFullNameById(ride.getDriver().getUserId());
                RideDTO.RideDTOBuilder builder = new RideDTO.RideDTOBuilder()
                        .setRideId(ride.getRideId())
                        .setRideStatus(ride.getRideStatus())
//                        .setPickLocationId(locationService.getLocationNameById(ride.getPickLocationId()))
//                        .setDropOffLocationId(locationService.getLocationNameById(ride.getDropOffLocationId()))
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

    public RideStatus getRideStatus(int rideId) throws DBException {
        return rideDAO.getRideStatus(rideId);
    }

    public DateRangeIncomeResponseDTO getIncomeByDateRange(int driverId, LocalDate startDate, LocalDate endDate) throws ApplicationException {
        List<Ride> rideDetails = rideDAO.getRidesByDateRange(driverId, startDate, endDate);
        int totalRides = rideDAO.getTotalRides(driverId, startDate, endDate);
        double totalEarning = rideDAO.getTotalEarnings(driverId, startDate, endDate);
        List<RideDTO> rideDTOS = toRideDTOList(rideDetails);
        return new DateRangeIncomeResponseDTO(totalRides, totalEarning, rideDTOS);
    }

    public List<RideDTO> toRideDTOList(List<Ride> rideList) {
        return rideList.stream()
                .map(ride -> {
                    try {
                        return toRideDTO(ride);
                    } catch (ApplicationException e) {
                        throw new RuntimeException(Message.Ride.ERROR_WHILE_CONVERTING_RIDE_TO_RIDEDTO, e);
                    }
                })
                .collect(Collectors.toList());
    }


    public RideDTO toRideDTO(Ride ride) throws ApplicationException {
        return new RideDTO.RideDTOBuilder()
                .setRideId(ride.getRideId())
                .setRideStatus(ride.getRideStatus())
//                .setPickLocationId(locationService.getLocationNameById(ride.getPickLocationId()))
//                .setDropOffLocationId(locationService.getLocationNameById(ride.getDropOffLocationId()))
//                .setCustomerName(userService.getUserNameById(ride.getCustomerId()))
//                .setDriverName(userService.getUserNameById(ride.getDriverId()))
                .setRideDate(ride.getRideDate())
                .setPickUpTime(ride.getPickUpTime())
                .setDisplayId(ride.getDisplayId())
                .setTotalKm(ride.getTotalKm())
                .setTotalCost(ride.getTotalCost())
                .setPaymentMode(ride.getPaymentMode())
                .setSystemEarning(ride.getSystemEarning())
                .setCancellationSystemEarning(ride.getCancellationSystemEarning())
                .setPaymentStatus(ride.getPaymentStatus())
                .setDriverEarning(ride.getDriverEarning())
                .setCancellationDriverEarning(ride.getCancellationDriverEarning())
                .setDriverPenalty(ride.getDriverPenalty())
                .build();
    }
}
