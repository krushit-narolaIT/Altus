package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.enums.FuelType;
import com.krushit.common.enums.VehicleType;
import com.krushit.common.enums.Transmission;
import com.krushit.common.exception.ValidationException;
import com.krushit.entity.*;

import java.time.LocalDateTime;

public class VehicleServicesValidator {

    private static boolean isNullOrEmpty(Object value) {
        return value == null || String.valueOf(value).trim().isEmpty();
    }

    public static void validateVehicleServiceDetails(VehicleService service) throws ValidationException {
        if (service == null) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_VEHICLE_SERVICE);
        }

        if (isNullOrEmpty(service.getServiceName())) {
            throw new ValidationException(Message.Vehicle.VEHICLE_SERVICE_IS_REQUIRED);
        }

        if (isNullOrEmpty(service.getBaseFare()) || service.getBaseFare() < 0) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_BASE_FARE);
        }

        if (isNullOrEmpty(service.getPerKmRate()) || service.getPerKmRate() < 0) {
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

        if (model.getVehicleService().getServiceId() <= 0) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_SERVICE_ID);
        }

        if (isNullOrEmpty(model.getBrandName())) {
            throw new ValidationException(Message.Vehicle.BRAND_NAME_IS_REQUIRED);
        }

        if (isNullOrEmpty(model.getModel())) {
            throw new ValidationException(Message.Vehicle.MODEL_NAME_IS_REQUIRED);
        }

        if (model.getMinYear() < 1990 || model.getMinYear() > LocalDateTime.now().getYear()) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_MIN_YEAR);
        }
    }

    public static void validateVehicleDetails(Vehicle vehicle) throws ValidationException {
        if (vehicle == null) {
            throw new ValidationException(Message.Vehicle.VEHICLE_DATA_MISSING);
        }

        if (isNullOrEmpty(vehicle.getBrandModel().getBrandModelId()) || vehicle.getBrandModel().getBrandModelId() <= 0) {
            throw new ValidationException(Message.Vehicle.BRAND_MODEL_ID_INVALID);
        }

        if (isNullOrEmpty(vehicle.getRegistrationNumber())) {
            throw new ValidationException(Message.Vehicle.REGISTRATION_NUMBER_REQUIRED);
        }

        if (!vehicle.getRegistrationNumber().matches("^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$")) {
            throw new ValidationException(Message.Vehicle.REGISTRATION_NUMBER_INVALID);
        }

        if (isNullOrEmpty(vehicle.getYear()) || vehicle.getYear() < 1990 || vehicle.getYear() > LocalDateTime.now().getYear()) {
            throw new ValidationException(Message.Vehicle.YEAR_INVALID);
        }

        if (!FuelType.isValidFuelType(vehicle.getFuelType())) {
            throw new ValidationException(Message.Vehicle.FUEL_TYPE_INVALID);
        }

        if (!Transmission.isValidTransmission(vehicle.getTransmission())) {
            throw new ValidationException(Message.Vehicle.TRANSMISSION_TYPE_INVALID);
        }

        if (isNullOrEmpty(vehicle.getGroundClearance()) || vehicle.getGroundClearance() <= 0) {
            throw new ValidationException(Message.Vehicle.GROUND_CLEARANCE_INVALID);
        }

        if (isNullOrEmpty(vehicle.getWheelBase()) || vehicle.getWheelBase() <= 0) {
            throw new ValidationException(Message.Vehicle.WHEEL_BASE_INVALID);
        }
    }
}
