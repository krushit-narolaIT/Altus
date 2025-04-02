package com.krushit.common.enums;

import com.krushit.common.Message;

public enum RideRequestStatus {
    PENDING("Pending"),
    ACCEPTED("Accepted"),
    CANCELLED("Cancelled"),
    REJECTED("Rejected");

    private final String status;

    RideRequestStatus(String status) {
        this.status = status;
    }

    public static RideRequestStatus getType(String status) {
        for (RideRequestStatus r : RideRequestStatus.values()) {
            if (r.status.equalsIgnoreCase(status)) {
                return r;
            }
        }
        throw new IllegalArgumentException(Message.Ride.INVALID_RIDE_STATUS + status);
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return status;
    }
}
