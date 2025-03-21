package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.model.Driver;

import java.io.File;

public class DriverDocumentValidator {
    public static void validateDriver(Driver driver) throws ValidationException {
        if (driver.getUserId() <= 0) {
            throw new ValidationException(Message.Driver.INVALID_DRIVER_ID);
        }
        validateLicenceNumber(driver.getLicenceNumber());
        validateLicencePhotoPath(driver.getLicencePhoto());
    }

    public static void validateLicenceNumber(String licenceNumber) throws ValidationException {
        if (licenceNumber == null || licenceNumber.trim().isEmpty()) {
            throw new ValidationException(Message.Driver.LICENCE_NUMBER_IS_REQUIRED);
        }
        if (licenceNumber.length() != 15) {
            throw new ValidationException(Message.Driver.ENTER_VALID_LICENCE_NUMBER);
        }
        if (!licenceNumber.matches("^[A-Za-z0-9]+$")) {
            throw new ValidationException(Message.Driver.ENTER_VALID_LICENCE_NUMBER);
        }
    }

    public static void validateLicencePhotoPath(String licencePhotoPath) throws ValidationException {
        if (licencePhotoPath == null || licencePhotoPath.isEmpty()) {
            throw new ValidationException(Message.Driver.LICENCE_NUMBER_IS_REQUIRED);
        }
        File file = new File(licencePhotoPath);
        if (!file.exists()) {
            throw new ValidationException(Message.Driver.UPLOAD_VALID_LICENCE_PHOTO + licencePhotoPath);
        }
    }
}
