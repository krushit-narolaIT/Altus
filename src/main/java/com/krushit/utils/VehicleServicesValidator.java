package com.krushit.utils;

import com.krushit.common.Message;
import com.krushit.exception.ValidationException;
import com.krushit.model.*;

import java.time.LocalDateTime;

public class VehicleServicesValidator {
    public static void validateVehicleServiceDetails(VehicleService service) throws ValidationException {
        if (service == null) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_VEHICLE_SERVICE);
        }
        
        if (service.getServiceName() == null || service.getServiceName().trim().isEmpty()) {
            throw new ValidationException(Message.Vehicle.VEHICLE_SERVICE_IS_REQUIRED);
        }

        if (String.valueOf(service.getBaseFare()).trim().isEmpty() || service.getBaseFare() < 0) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_BASE_FARE);
        }

        if (String.valueOf(service.getPerKmRate()).trim().isEmpty() || service.getPerKmRate() < 0) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_PER_KM_RATE);
        }

        if (!VehicleType.isValidVehicleType(service.getVehicleType())) {
            throw new ValidationException(Message.Vehicle.INVALID_VEHICLE_TYPE);
        }
        
        if (service.getMaxPassengers() <= 0 || service.getMaxPassengers() > 8) {
            throw new ValidationException(Message.Vehicle.INVALID_PASSENGER_CAPACITY);
        }
        
        if (service.getCommissionPercentage() < 0 || service.getCommissionPercentage() > 100) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_COMMISSION_PERCENTAGE);
        }
    }

    public static void validateVehicleModelDetails(BrandModel model) throws ValidationException {
        if (model == null) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_BRAND_MODEL);
        }

        if (model.getServiceId() <= 0) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_SERVICE_ID);
        }

        if (model.getBrandName() == null || model.getBrandName().trim().isEmpty()) {
            throw new ValidationException(Message.Vehicle.BRAND_NAME_IS_REQUIRED);
        }

        if (model.getModel() == null || model.getModel().trim().isEmpty()) {
            throw new ValidationException(Message.Vehicle.MODEL_NAME_IS_REQUIRED);
        }

        if (model.getMinYear() > 2000 || model.getMinYear() < LocalDateTime.now().getYear()) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_MIN_YEAR);
        }
    }

    public static void validateVehicleDetails(Vehicle vehicle) throws ValidationException {
        if (vehicle == null) {
            throw new ValidationException(Message.Vehicle.VEHICLE_DATA_MISSING);
        }

        if (String.valueOf(vehicle.getDriverId()).trim().isEmpty() || vehicle.getDriverId() <= 0) {
            throw new ValidationException(Message.Vehicle.DRIVER_ID_INVALID);
        }

        if (String.valueOf(vehicle.getBrandModelId()).trim().isEmpty() || vehicle.getBrandModelId() <= 0) {
            throw new ValidationException(Message.Vehicle.BRAND_MODEL_ID_INVALID);
        }

        if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().trim().isEmpty()) {
            throw new ValidationException(Message.Vehicle.REGISTRATION_NUMBER_REQUIRED);
        }

        if (!vehicle.getRegistrationNumber().matches("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$")) {
            throw new ValidationException(Message.Vehicle.REGISTRATION_NUMBER_INVALID);
        }

        if (String.valueOf(vehicle.getYear()).trim().isEmpty() || vehicle.getYear() > 1990 || vehicle.getYear() < LocalDateTime.now().getYear()) {
            throw new ValidationException(Message.Vehicle.YEAR_INVALID);
        }

        if (!FuelType.isValidFuelType(vehicle.getFuelType())) {
            throw new ValidationException(Message.Vehicle.FUEL_TYPE_INVALID);
        }

        if (!Transmission.isValidTransmission(vehicle.getTransmission())) {
            throw new ValidationException(Message.Vehicle.TRANSMISSION_TYPE_INVALID);
        }

        if (String.valueOf(vehicle.getGroundClearance()).trim().isEmpty() || vehicle.getGroundClearance() <= 0) {
            throw new ValidationException(Message.Vehicle.GROUND_CLEARANCE_INVALID);
        }

        if (String.valueOf(vehicle.getWheelBase()).trim().isEmpty()|| vehicle.getWheelBase() <= 0) {
            throw new ValidationException(Message.Vehicle.WHEEL_BASE_INVALID);
        }
    }

}
