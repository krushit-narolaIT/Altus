package com.krushit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Driver extends User {
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

    public Driver() {
        super();
    }

    public Driver(int userId, Role role, String firstName, String lastName, String phoneNo, String emailId,
                  String password, boolean isActive, String displayId, LocalDateTime createdAt,
                  LocalDateTime updatedAt, String createdBy, String updatedBy,
                  int driverId, String licenceNumber, boolean isDocumentVerified, String licencePhoto,
                  boolean isAvailable, String verificationStatus, String comment) {
        super(userId, role, firstName, lastName, phoneNo, emailId, password, isActive, displayId, createdAt, updatedAt, createdBy, updatedBy);
        this.driverId = driverId;
        this.licenceNumber = licenceNumber;
        this.isDocumentVerified = isDocumentVerified;
        this.licencePhoto = licencePhoto;
        this.isAvailable = isAvailable;
        this.verificationStatus = verificationStatus;
        this.comment = comment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String licenceNumber) { this.licenceNumber = licenceNumber; }

    public boolean isDocumentVerified() { return isDocumentVerified; }
    public void setDocumentVerified(boolean documentVerified) { isDocumentVerified = documentVerified; }

    public String getLicencePhoto() { return licencePhoto; }
    public void setLicencePhoto(String licencePhoto) { this.licencePhoto = licencePhoto; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
