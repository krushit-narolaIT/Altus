package com.krushit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.krushit.common.enums.PaymentMode;
import com.krushit.common.enums.PaymentStatus;
import com.krushit.common.enums.RideStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = RideDTO.RideDTOBuilder.class)
public class RideDTO {
    private final int rideId;
    private final RideStatus rideStatus;
    private final String pickLocationId;
    private final String dropOffLocationId;
    private final String customerName;
    private final String driverName;
    private final LocalDate rideDate;
    private final LocalTime pickUpTime;
    private final LocalTime dropOffTime;
    private final String displayId;
    private final double totalKm;
    private final double totalCost;
    private final PaymentMode paymentMode;
    private final PaymentStatus paymentStatus;
    private final double cancellationCharge;
    private final Double driverEarning;
    private final Double cancellationDriverEarning;
    private final Double driverPenalty;

    private RideDTO(RideDTOBuilder builder) {
        this.rideId = builder.rideId;
        this.rideStatus = builder.rideStatus;
        this.pickLocationId = builder.pickLocationId;
        this.dropOffLocationId = builder.dropOffLocationId;
        this.customerName = builder.customerName;
        this.driverName = builder.driverName;
        this.rideDate = builder.rideDate;
        this.pickUpTime = builder.pickUpTime;
        this.dropOffTime = builder.dropOffTime;
        this.displayId = builder.displayId;
        this.totalKm = builder.totalKm;
        this.totalCost = builder.totalCost;
        this.paymentMode = builder.paymentMode;
        this.paymentStatus = builder.paymentStatus;
        this.cancellationCharge = builder.cancellationCharge;
        this.driverEarning = builder.driverEarning;
        this.cancellationDriverEarning = builder.cancellationDriverEarning;
        this.driverPenalty = builder.driverPenalty;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class RideDTOBuilder {
        private int rideId;
        private RideStatus rideStatus;
        private String pickLocationId;
        private String dropOffLocationId;
        private String customerName;
        private String driverName;
        private LocalDate rideDate;
        private LocalTime pickUpTime;
        private LocalTime dropOffTime;
        private String displayId;
        private double totalKm;
        private double totalCost;
        private PaymentMode paymentMode;
        private PaymentStatus paymentStatus;
        private double cancellationCharge;
        private Double driverEarning;
        private Double cancellationDriverEarning;
        private Double driverPenalty;

        public RideDTOBuilder setRideId(int rideId) {
            this.rideId = rideId;
            return this;
        }

        public RideDTOBuilder setRideStatus(RideStatus rideStatus) {
            this.rideStatus = rideStatus;
            return this;
        }

        public RideDTOBuilder setPickLocationId(String pickLocationId) {
            this.pickLocationId = pickLocationId;
            return this;
        }

        public RideDTOBuilder setDropOffLocationId(String dropOffLocationId) {
            this.dropOffLocationId = dropOffLocationId;
            return this;
        }

        public RideDTOBuilder setCustomerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public RideDTOBuilder setDriverName(String driverName) {
            this.driverName = driverName;
            return this;
        }

        public RideDTOBuilder setRideDate(LocalDate rideDate) {
            this.rideDate = rideDate;
            return this;
        }

        public RideDTOBuilder setPickUpTime(LocalTime pickUpTime) {
            this.pickUpTime = pickUpTime;
            return this;
        }

        public RideDTOBuilder setDropOffTime(LocalTime dropOffTime) {
            this.dropOffTime = dropOffTime;
            return this;
        }

        public RideDTOBuilder setDisplayId(String displayId) {
            this.displayId = displayId;
            return this;
        }

        public RideDTOBuilder setTotalKm(double totalKm) {
            this.totalKm = totalKm;
            return this;
        }

        public RideDTOBuilder setTotalCost(double totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public RideDTOBuilder setPaymentMode(PaymentMode paymentMode) {
            this.paymentMode = paymentMode;
            return this;
        }

        public RideDTOBuilder setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public RideDTOBuilder setCancellationCharge(double cancellationCharge) {
            this.cancellationCharge = cancellationCharge;
            return this;
        }

        public RideDTOBuilder setDriverEarning(Double driverEarning) {
            this.driverEarning = driverEarning;
            return this;
        }

        public RideDTOBuilder setCancellationDriverEarning(Double cancellationDriverEarning) {
            this.cancellationDriverEarning = cancellationDriverEarning;
            return this;
        }

        public RideDTOBuilder setDriverPenalty(Double driverPenalty) {
            this.driverPenalty = driverPenalty;
            return this;
        }

        public RideDTO build() {
            return new RideDTO(this);
        }
    }

    // Getters
    public int getRideId() {
        return rideId;
    }

    public RideStatus getRideStatus() {
        return rideStatus;
    }

    public String getPickLocationId() {
        return pickLocationId;
    }

    public String getDropOffLocationId() {
        return dropOffLocationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDriverName() {
        return driverName;
    }

    public LocalDate getRideDate() {
        return rideDate;
    }

    public LocalTime getPickUpTime() {
        return pickUpTime;
    }

    public LocalTime getDropOffTime() {
        return dropOffTime;
    }

    public String getDisplayId() {
        return displayId;
    }

    public double getTotalKm() {
        return totalKm;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public double getCancellationCharge() {
        return cancellationCharge;
    }

    public Double getDriverEarning() {
        return driverEarning;
    }

    public Double getCancellationDriverEarning() {
        return cancellationDriverEarning;
    }

    public Double getDriverPenalty() {
        return driverPenalty;
    }
}
