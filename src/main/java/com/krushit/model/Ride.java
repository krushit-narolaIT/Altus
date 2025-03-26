package com.krushit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
import com.krushit.common.enums.RideStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Ride.RideBuilder.class)
public class Ride {
    private int rideId;
    private RideStatus rideStatus;
    private int pickLocationId;
    private int dropOffLocationId;
    private int customerId;
    private int driverId;
    private LocalDate rideDate;
    private LocalTime pickUpTime;
    private LocalTime dropOffTime;
    private String displayId;
    private double totalKm;
    private double totalCost;
    private PaymentMode paymentMode;
    private PaymentStatus paymentStatus;
    private double commissionPercentage;
    private double driverEarning;
    private double systemEarning;
    private double cancellationCharge;
    private double cancellationDriverEarning;
    private double cancellationSystemEarning;
    private double driverPenalty;

    private Ride(RideBuilder builder) {
        this.rideId = builder.rideId;
        this.rideStatus = builder.rideStatus;
        this.pickLocationId = builder.pickLocationId;
        this.dropOffLocationId = builder.dropOffLocationId;
        this.customerId = builder.customerId;
        this.driverId = builder.driverId;
        this.rideDate = builder.rideDate;
        this.pickUpTime = builder.pickUpTime;
        this.dropOffTime = builder.dropOffTime;
        this.displayId = builder.displayId;
        this.totalKm = builder.totalKm;
        this.totalCost = builder.totalCost;
        this.paymentMode = builder.paymentMode;
        this.paymentStatus = builder.paymentStatus;
        this.commissionPercentage = builder.commissionPercentage;
        this.driverEarning = builder.driverEarning;
        this.systemEarning = builder.systemEarning;
        this.cancellationCharge = builder.cancellationCharge;
        this.cancellationDriverEarning = builder.cancellationDriverEarning;
        this.cancellationSystemEarning = builder.cancellationSystemEarning;
        this.driverPenalty = builder.driverPenalty;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class RideBuilder {
        private int rideId;
        private RideStatus rideStatus;
        private int pickLocationId;
        private int dropOffLocationId;
        private int customerId;
        private int driverId;
        private LocalDate rideDate;
        private LocalTime pickUpTime;
        private LocalTime dropOffTime;
        private String displayId;
        private double totalKm;
        private double totalCost;
        private PaymentMode paymentMode;
        private PaymentStatus paymentStatus;
        private double commissionPercentage;
        private double driverEarning;
        private double systemEarning;
        private double cancellationCharge;
        private double cancellationDriverEarning;
        private double cancellationSystemEarning;
        private double driverPenalty;

        public RideBuilder setRideId(int rideId) { this.rideId = rideId; return this; }
        public RideBuilder setRideStatus(RideStatus rideStatus) { this.rideStatus = rideStatus; return this; }
        public RideBuilder setPickLocationId(int pickLocationId) { this.pickLocationId = pickLocationId; return this; }
        public RideBuilder setDropOffLocationId(int dropOffLocationId) { this.dropOffLocationId = dropOffLocationId; return this; }
        public RideBuilder setCustomerId(int customerId) { this.customerId = customerId; return this; }
        public RideBuilder setDriverId(int driverId) { this.driverId = driverId; return this; }
        public RideBuilder setRideDate(LocalDate rideDate) { this.rideDate = rideDate; return this; }
        public RideBuilder setPickUpTime(LocalTime pickUpTime) { this.pickUpTime = pickUpTime; return this; }
        public RideBuilder setDropOffTime(LocalTime dropOffTime) { this.dropOffTime = dropOffTime; return this; }
        public RideBuilder setDisplayId(String displayId) { this.displayId = displayId; return this; }
        public RideBuilder setTotalKm(double totalKm) { this.totalKm = totalKm; return this; }
        public RideBuilder setTotalCost(double totalCost) { this.totalCost = totalCost; return this; }
        public RideBuilder setPaymentMode(PaymentMode paymentMode) { this.paymentMode = paymentMode; return this; }
        public RideBuilder setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; return this; }
        public RideBuilder setCommissionPercentage(double commissionPercentage) { this.commissionPercentage = commissionPercentage; return this; }
        public RideBuilder setDriverEarning(double driverEarning) { this.driverEarning = driverEarning; return this; }
        public RideBuilder setSystemEarning(double systemEarning) { this.systemEarning = systemEarning; return this; }
        public RideBuilder setCancellationCharge(double cancellationCharge) { this.cancellationCharge = cancellationCharge; return this; }
        public RideBuilder setCancellationDriverEarning(double cancellationDriverEarning) { this.cancellationDriverEarning = cancellationDriverEarning; return this; }
        public RideBuilder setCancellationSystemEarning(double cancellationSystemEarning) { this.cancellationSystemEarning = cancellationSystemEarning; return this; }
        public RideBuilder setDriverPenalty(double driverPenalty) { this.driverPenalty = driverPenalty; return this; }

        public Ride build() {
            return new Ride(this);
        }
    }

    // Getters
    public int getRideId() { return rideId; }
    public RideStatus getRideStatus() { return rideStatus; }
    public int getPickLocationId() { return pickLocationId; }
    public int getDropOffLocationId() { return dropOffLocationId; }
    public int getCustomerId() { return customerId; }
    public int getDriverId() { return driverId; }
    public LocalDate getRideDate() { return rideDate; }
    public LocalTime getPickUpTime() { return pickUpTime; }
    public LocalTime getDropOffTime() { return dropOffTime; }
    public String getDisplayId() { return displayId; }
    public double getTotalKm() { return totalKm; }
    public double getTotalCost() { return totalCost; }
    public PaymentMode getPaymentMode() { return paymentMode; }
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public double getCommissionPercentage() { return commissionPercentage; }
    public double getDriverEarning() { return driverEarning; }
    public double getSystemEarning() { return systemEarning; }
    public double getCancellationCharge() { return cancellationCharge; }
    public double getCancellationDriverEarning() { return cancellationDriverEarning; }
    public double getCancellationSystemEarning() { return cancellationSystemEarning; }
    public double getDriverPenalty() { return driverPenalty; }

    @Override
    public String toString() {
        return "Ride{" +
                "rideId=" + rideId +
                ", rideStatus=" + rideStatus +
                ", pickLocationId=" + pickLocationId +
                ", dropOffLocationId=" + dropOffLocationId +
                ", customerId=" + customerId +
                ", driverId=" + driverId +
                ", rideDate=" + rideDate +
                ", pickUpTime=" + pickUpTime +
                ", dropOffTime=" + dropOffTime +
                ", displayId='" + displayId + '\'' +
                ", totalKm=" + totalKm +
                ", totalCost=" + totalCost +
                ", paymentMode=" + paymentMode +
                ", paymentStatus=" + paymentStatus +
                ", commissionPercentage=" + commissionPercentage +
                ", driverEarning=" + driverEarning +
                ", systemEarning=" + systemEarning +
                ", cancellationCharge=" + cancellationCharge +
                ", cancellationDriverEarning=" + cancellationDriverEarning +
                ", cancellationSystemEarning=" + cancellationSystemEarning +
                ", driverPenalty=" + driverPenalty +
                '}';
    }
}
