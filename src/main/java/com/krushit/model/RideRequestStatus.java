package com.krushit.model;

public enum RideRequestStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String status;

    RideRequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static RideRequestStatus fromString(String status) {
        for (RideRequestStatus r : RideRequestStatus.values()) {
            if (r.status.equalsIgnoreCase(status)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Invalid RideRequestStatus: " + status);
    }
}
