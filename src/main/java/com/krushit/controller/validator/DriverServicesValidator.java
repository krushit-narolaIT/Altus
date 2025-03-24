package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.dto.DriverVerificationRequest;
import com.krushit.common.exception.ValidationException;

public class DriverServicesValidator {
    public static void validateDriverApprovalRequest(DriverVerificationRequest verificationRequest) throws ValidationException {
        if (verificationRequest  == null) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_VERIFICATION_REQUEST);
        }

        if (isNullOrEmpty(verificationRequest.getDriverId()) || verificationRequest.getDriverId() < 0) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_DRIVER_ID);
        }

        if (verificationRequest.getVerificationStatus() == null ||
                verificationRequest.getVerificationStatus().trim().isEmpty() ||
                !(verificationRequest.getVerificationStatus().equalsIgnoreCase("accept") ||
                        verificationRequest.getVerificationStatus().equalsIgnoreCase("reject"))) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_VERIFICATION_STATUS);
        }

        if (verificationRequest.getVerificationStatus().equalsIgnoreCase("reject")) {
            if (verificationRequest.getMessage() == null || verificationRequest.getMessage().trim().isEmpty()) {
                throw new ValidationException(Message.Vehicle.PLEASE_ENTER_REJECTION_MESSAGE);
            }
        }
    }

    private static boolean isNullOrEmpty(Object value) {
        return value == null || String.valueOf(value).trim().isEmpty();
    }
}
