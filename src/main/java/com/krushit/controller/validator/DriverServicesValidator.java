package com.krushit.controller.validator;

import com.krushit.common.Message;
import com.krushit.common.enums.DocumentVerificationStatus;
import com.krushit.dto.DriverVerificationRequestDTO;
import com.krushit.common.exception.ValidationException;

public class DriverServicesValidator {
    public static void validateDriverApprovalRequest(DriverVerificationRequestDTO verificationRequest) throws ValidationException {
        if (verificationRequest  == null) {
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_VERIFICATION_REQUEST);
        }

        if (verificationRequest.getVerificationStatus() == null ||
                verificationRequest.getVerificationStatus().trim().isEmpty() ||
                !(verificationRequest.getVerificationStatus().equalsIgnoreCase(DocumentVerificationStatus.VERIFIED.name()) ||
                        verificationRequest.getVerificationStatus().equalsIgnoreCase(DocumentVerificationStatus.REJECTED.name()))){
            throw new ValidationException(Message.Vehicle.PLEASE_ENTER_VALID_VERIFICATION_STATUS);
        }

        if (verificationRequest.getVerificationStatus().equalsIgnoreCase(DocumentVerificationStatus.REJECTED.name())) {
            if (verificationRequest.getMessage() == null || verificationRequest.getMessage().trim().isEmpty()) {
                throw new ValidationException(Message.Vehicle.PLEASE_ENTER_REJECTION_MESSAGE);
            }
        }
    }
}
