package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ApplicationException;
import com.krushit.common.exception.ValidationException;
import com.krushit.dto.DistanceCalculatorDTO;
import com.krushit.dto.RideRequestDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RideValidator {
    public static void validateLocation(DistanceCalculatorDTO distanceCalculatorDTO) throws ValidationException {
        if (distanceCalculatorDTO == null || String.valueOf(distanceCalculatorDTO.getFrom()).trim().isEmpty() ||
                String.valueOf(distanceCalculatorDTO.getTo()).trim().isEmpty()) {
            throw new ValidationException(Message.Ride.LOCATION_IS_REQUIRE);
        }
    }

    public static void validateRideRequest(RideRequestDTO rideRequestDTO) throws ApplicationException {
        if (rideRequestDTO == null) {
            throw new ApplicationException(Message.Ride.RIDE_REQUEST_NULL);
        }

        if (isNullOrEmpty(rideRequestDTO.getPickUpLocationId()) || rideRequestDTO.getPickUpLocationId() <= 0) {
            throw new ApplicationException(Message.Ride.INVALID_PICKUP_LOCATION_ID);
        }

        if (isNullOrEmpty(rideRequestDTO.getDropOffLocationId()) || rideRequestDTO.getDropOffLocationId() <= 0) {
            throw new ApplicationException(Message.Ride.INVALID_DROP_OFF_LOCATION_ID);
        }

        if (String.valueOf(rideRequestDTO.getPickUpLocationId()).trim().equals(String.valueOf(rideRequestDTO.getDropOffLocationId()).trim())) {
            throw new ApplicationException(Message.Ride.SAME_PICKUP_DROP_OFF);
        }

        if (isNullOrEmpty(rideRequestDTO.getVehicleServiceId()) || rideRequestDTO.getVehicleServiceId() <= 0) {
            throw new ApplicationException(Message.Ride.INVALID_VEHICLE_SERVICE_ID);
        }

        if (isNullOrEmpty(rideRequestDTO.getUserId()) || rideRequestDTO.getUserId() <= 0) {
            throw new ApplicationException(Message.Ride.INVALID_USER_ID);
        }

        if (rideRequestDTO.getRideDate() == null) {
            throw new ApplicationException(Message.Ride.RIDE_DATE_REQUIRED);
        }

        LocalDate rideDate;
        String rideDateStr = rideRequestDTO.getRideDate().toString();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            rideDate = LocalDate.parse(rideDateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new ApplicationException(Message.Ride.PLEASE_ENTER_VALID_RIDE_FORMAT);
        }

        if (rideDate.isBefore(LocalDate.now())) {
            throw new ApplicationException(Message.Ride.RIDE_DATE_IN_PAST);
        }

        if (rideDate.isAfter(LocalDate.now().plusDays(15))) {
            throw new ApplicationException(Message.Ride.BOOK_RIDE_IN_ONLY_FIFTEEN_DAYS_IN_ADVANCE);
        }

        if (rideRequestDTO.getPickUpTime() == null) {
            throw new ApplicationException(Message.Ride.PICKUP_TIME_REQUIRED);
        }

        LocalTime pickUpTime = rideRequestDTO.getPickUpTime();

        if (pickUpTime.isBefore(LocalTime.now()) && rideDate.equals(LocalDate.now())) {
            throw new ApplicationException(Message.Ride.PICKUP_TIME_IN_PAST);
        }
    }

    private static boolean isNullOrEmpty(Object value) {
        return value == null || String.valueOf(value).trim().isEmpty();
    }
}