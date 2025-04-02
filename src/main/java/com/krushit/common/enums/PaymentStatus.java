package com.krushit.common.enums;

import com.krushit.common.Message;

public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid"),
    FAILED("Failed");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public static PaymentStatus getType(String status) {
        for (PaymentStatus p : PaymentStatus.values()) {
            if (p.status.equalsIgnoreCase(status)) {
                return p;
            }
        }
        throw new IllegalArgumentException(Message.INVALID_PAYMENT_STATUS + status);
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
