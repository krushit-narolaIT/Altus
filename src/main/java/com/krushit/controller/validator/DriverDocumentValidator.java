package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.exception.ValidationException;
import com.krushit.entity.Driver;
import jakarta.servlet.http.Part;

public class DriverDocumentValidator {
    public static void validateDriverDocuments(Driver driver, Part licencePhoto) throws ValidationException {
        if (driver.getUserId() <= 0) {
            throw new ValidationException(Message.Driver.INVALID_DRIVER_ID);
        }
        validateLicenceNumber(driver.getLicenceNumber());
        validateLicencePhoto(licencePhoto);
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

    public static void validateLicencePhoto(Part licencePhoto) throws ValidationException {
        if (licencePhoto == null || licencePhoto.getSize() == 0) {
            throw new ValidationException(Message.Driver.LICENCE_PHOTO_PATH_IS_REQUIRD);
        }
        String fileName = licencePhoto.getSubmittedFileName();
        if (fileName == null || fileName.isEmpty()) {
            throw new ValidationException(Message.Driver.UPLOAD_VALID_LICENCE_PHOTO);
        }
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.matches("jpg|jpeg|png")) {
            throw new ValidationException(Message.Driver.INVALID_FILE_TYPE);
        }
    }
}
