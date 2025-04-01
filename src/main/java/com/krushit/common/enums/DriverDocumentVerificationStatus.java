package com.krushit.common.enums;

import com.krushit.common.Message;

public enum DriverDocumentVerificationStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    private final String status;

    DriverDocumentVerificationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static DriverDocumentVerificationStatus getType(String status) {
        for (DriverDocumentVerificationStatus r : DriverDocumentVerificationStatus.values()) {
            if (r.status.equalsIgnoreCase(status)) {
                return r;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_DRIVER_VERIFICATION_STATUS + status);
    }

    public boolean isRejected(){
        return  true;
    }
}
