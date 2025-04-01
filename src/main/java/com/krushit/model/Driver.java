package com.krushit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = Driver.DriverBuilder.class)
public class Driver extends User {
    private final int driverId;
    private final String licenceNumber;
    private final boolean isDocumentVerified;
    private final String licencePhoto;
    private final boolean isAvailable;
    private final String verificationStatus;
    private final String comment;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String createdBy;
    private final String updatedBy;

    private Driver(DriverBuilder builder) {
        super(builder);
        this.driverId = builder.driverId;
        this.licenceNumber = builder.licenceNumber;
        this.isDocumentVerified = builder.isDocumentVerified;
        this.licencePhoto = builder.licencePhoto;
        this.isAvailable = builder.isAvailable;
        this.verificationStatus = builder.verificationStatus;
        this.comment = builder.comment;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.createdBy = builder.createdBy;
        this.updatedBy = builder.updatedBy;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public boolean isDocumentVerified() {
        return isDocumentVerified;
    }

    public String getLicencePhoto() {
        return licencePhoto;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId=" + driverId +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", isDocumentVerified=" + isDocumentVerified +
                ", licencePhoto='" + licencePhoto + '\'' +
                ", isAvailable=" + isAvailable +
                ", verificationStatus='" + verificationStatus + '\'' +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class DriverBuilder extends UserBuilder {
        private int driverId;
        private String licenceNumber;
        private boolean isDocumentVerified;
        private String licencePhoto;
        private boolean isAvailable;
        private String verificationStatus;
        private String comment;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        public DriverBuilder setDriverId(int driverId) {
            this.driverId = driverId;
            return this;
        }

        public DriverBuilder setLicenceNumber(String licenceNumber) {
            this.licenceNumber = licenceNumber;
            return this;
        }

        public DriverBuilder setDocumentVerified(boolean isDocumentVerified) {
            this.isDocumentVerified = isDocumentVerified;
            return this;
        }

        public DriverBuilder setLicencePhoto(String licencePhoto) {
            this.licencePhoto = licencePhoto;
            return this;
        }

        public DriverBuilder setAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public DriverBuilder setVerificationStatus(String verificationStatus) {
            this.verificationStatus = verificationStatus;
            return this;
        }

        public DriverBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public DriverBuilder setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DriverBuilder setUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DriverBuilder setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DriverBuilder setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        @Override
        public Driver build() {
            return new Driver(this);
        }
    }
}
