package com.krushit.dto;

public class RideCancellationDetails {
    private final int rideId;
    private final String rideStatus;
    private final double cancellationCharge;
    private final double driverEarning;
    private final double systemEarning;
    private final double driverPenalty;

    private RideCancellationDetails(RideCancellationDetailsBuilder builder) {
        this.rideId = builder.rideId;
        this.rideStatus = builder.rideStatus;
        this.cancellationCharge = builder.cancellationCharge;
        this.driverEarning = builder.driverEarning;
        this.systemEarning = builder.systemEarning;
        this.driverPenalty = builder.driverPenalty;
    }

    public int getRideId() {
        return rideId;
    }

    public String getRideStatus() {
        return rideStatus;
    }

    public double getCancellationCharge() {
        return cancellationCharge;
    }

    public double getDriverEarning() {
        return driverEarning;
    }

    public double getSystemEarning() {
        return systemEarning;
    }

    public double getDriverPenalty() {
        return driverPenalty;
    }

    public static class RideCancellationDetailsBuilder {
        private int rideId;
        private String rideStatus;
        private double cancellationCharge;
        private double driverEarning;
        private double systemEarning;
        private double driverPenalty;

        public RideCancellationDetailsBuilder setRideId(int rideId) {
            this.rideId = rideId;
            return this;
        }

        public RideCancellationDetailsBuilder setRideStatus(String rideStatus) {
            this.rideStatus = rideStatus;
            return this;
        }

        public RideCancellationDetailsBuilder setCancellationCharge(double cancellationCharge) {
            this.cancellationCharge = cancellationCharge;
            return this;
        }

        public RideCancellationDetailsBuilder setDriverEarning(double driverEarning) {
            this.driverEarning = driverEarning;
            return this;
        }

        public RideCancellationDetailsBuilder setSystemEarning(double systemEarning) {
            this.systemEarning = systemEarning;
            return this;
        }

        public RideCancellationDetailsBuilder setDriverPenalty(double driverPenalty) {
            this.driverPenalty = driverPenalty;
            return this;
        }

        public RideCancellationDetails build() {
            return new RideCancellationDetails(this);
        }
    }
}
