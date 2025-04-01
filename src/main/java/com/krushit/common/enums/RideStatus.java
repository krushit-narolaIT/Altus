package com.krushit.common.enums;

public enum RideStatus {
    SCHEDULED("Scheduled"),
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String status;

    RideStatus(String status) {
        this.status = status;
    }

    public static RideStatus getType(String status) {
        for (RideStatus r : RideStatus.values()) {
            if (r.status.equalsIgnoreCase(status)) {
                return r;
            }
        }
        return null;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
