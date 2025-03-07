package com.krushit.entity;

import java.sql.Blob;
import java.time.LocalDateTime;

public class Driver extends User {
    private int driverId;
    private String licenseNumber;
    private boolean isDocumentVerified;
    private Blob licensePhoto;
    private boolean isAvailable;
    private String verificationStatus;
    private String comment;

    public Driver() {
        super();
    }

    public Driver(int userId, Role roleId, String firstName, String lastName, String phoneNo, String emailId, String password, boolean isActive, String displayId, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, int driverId, String licenseNumber, boolean isDocumentVerified, Blob licensePhoto, boolean isAvailable, String verificationStatus, String comment) {
        super(userId, roleId, firstName, lastName, phoneNo, emailId, password, isActive, displayId, createdAt, updatedAt, createdBy, updatedBy);
        this.driverId = driverId;
        this.licenseNumber = licenseNumber;
        this.isDocumentVerified = isDocumentVerified;
        this.licensePhoto = licensePhoto;
        this.isAvailable = isAvailable;
        this.verificationStatus = verificationStatus;
        this.comment = comment;
    }

    public int getDriverId() { return driverId; }
    public void setDriverId(int driverId) { this.driverId = driverId; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public boolean isDocumentVerified() { return isDocumentVerified; }
    public void setDocumentVerified(boolean documentVerified) { isDocumentVerified = documentVerified; }

    public Blob getLicensePhoto() { return licensePhoto; }
    public void setLicensePhoto(Blob licensePhoto) { this.licensePhoto = licensePhoto; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
