package com.krushit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = RideRequest.RideRequestBuilder.class)
public class RideRequest {
    private final int rideRequestId;
    private final RideRequestStatus rideRequestStatus;
    private final int pickUpLocationId;
    private final int dropOffLocationId;
    private final int vehicleServiceId;
    private final int userId;
    private final LocalDate rideDate;
    private final LocalTime pickUpTime;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private RideRequest(RideRequestBuilder builder) {
        this.rideRequestId = builder.rideRequestId;
        this.rideRequestStatus = builder.rideRequestStatus;
        this.pickUpLocationId = builder.pickUpLocationId;
        this.dropOffLocationId = builder.dropOffLocationId;
        this.vehicleServiceId = builder.vehicleServiceId;
        this.userId = builder.userId;
        this.rideDate = builder.rideDate;
        this.pickUpTime = builder.pickUpTime;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class RideRequestBuilder {
        private int rideRequestId;
        private RideRequestStatus rideRequestStatus;
        private int pickUpLocationId;
        private int dropOffLocationId;
        private int vehicleServiceId;
        private int userId;
        private LocalDate rideDate;
        private LocalTime pickUpTime;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public RideRequestBuilder setRideRequestId(int rideRequestId) {
            this.rideRequestId = rideRequestId;
            return this;
        }

        public RideRequestBuilder setRideRequestStatus(RideRequestStatus rideRequestStatus) {
            this.rideRequestStatus = rideRequestStatus;
            return this;
        }

        public RideRequestBuilder setPickUpLocationId(int pickUpLocationId) {
            this.pickUpLocationId = pickUpLocationId;
            return this;
        }

        public RideRequestBuilder setDropOffLocationId(int dropOffLocationId) {
            this.dropOffLocationId = dropOffLocationId;
            return this;
        }

        public RideRequestBuilder setVehicleServiceId(int vehicleServiceId) {
            this.vehicleServiceId = vehicleServiceId;
            return this;
        }

        public RideRequestBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public RideRequestBuilder setRideDate(LocalDate rideDate) {
            this.rideDate = rideDate;
            return this;
        }

        public RideRequestBuilder setPickUpTime(LocalTime pickUpTime) {
            this.pickUpTime = pickUpTime;
            return this;
        }

        public RideRequestBuilder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RideRequestBuilder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RideRequest build() {
            return new RideRequest(this);
        }
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "rideRequestId=" + rideRequestId +
                ", rideRequestStatus=" + rideRequestStatus +
                ", pickUpLocationId=" + pickUpLocationId +
                ", dropOffLocationId=" + dropOffLocationId +
                ", vehicleServiceId=" + vehicleServiceId +
                ", userId=" + userId +
                ", rideDate=" + rideDate +
                ", pickUpTime=" + pickUpTime +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
