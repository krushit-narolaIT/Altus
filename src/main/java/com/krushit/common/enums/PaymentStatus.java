package com.krushit.common.enums;

public enum PaymentStatus {
    PENDING("Pending"),
    PAID("Paid"),
    FAILED("Failed");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static PaymentStatus fromString(String status) {
        for (PaymentStatus p : PaymentStatus.values()) {
            if (p.status.equalsIgnoreCase(status)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentStatus: " + status);
    }
}
