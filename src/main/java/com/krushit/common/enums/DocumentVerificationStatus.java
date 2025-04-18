package com.krushit.common.enums;

import com.krushit.common.Message;

public enum DocumentVerificationStatus {
    PENDING("Pending"),
    VERIFIED("Verified"),
    REJECTED("Rejected"),
    INCOMPLETE("Incomplete");

    private final String status;

    DocumentVerificationStatus(String status) {
        this.status = status;
    }

    public static DocumentVerificationStatus getType(String status) {
        for (DocumentVerificationStatus r : DocumentVerificationStatus.values()) {
            if (r.status.equalsIgnoreCase(status)) {
                return r;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_DRIVER_VERIFICATION_STATUS + status);
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    public boolean isRejected() {
        return true;
    }
}
