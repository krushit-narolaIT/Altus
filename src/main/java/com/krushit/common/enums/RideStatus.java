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

    public static String getType(RideStatus status) {
        for (RideStatus r : RideStatus.values()) {
            if (r == status) {
                return r.getStatus();
            }
        }
        return null;
    }

    public static RideStatus getType(String status) {
        for (RideStatus rs : RideStatus.values()) {
            if (rs.name().equalsIgnoreCase(status)) {
                return rs;
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
