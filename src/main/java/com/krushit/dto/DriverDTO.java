package com.krushit.dto;

public class DriverDTO {
    private final int userId;
    private final String role;
    private final String firstName;
    private final String lastName;
    private final String phoneNo;
    private final String emailId;
    private final String displayId;
    private final String licenceNumber;
    private final String licencePhoto;
    private final boolean documentVerified;

    private DriverDTO(DriverDTOBuilder builder) {
        this.userId = builder.userId;
        this.role = builder.role;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNo = builder.phoneNo;
        this.emailId = builder.emailId;
        this.displayId = builder.displayId;
        this.licenceNumber = builder.licenceNumber;
        this.licencePhoto = builder.licencePhoto;
        this.documentVerified = builder.documentVerified;
    }

    public static class DriverDTOBuilder {
        private int userId;
        private String role;
        private String firstName;
        private String lastName;
        private String phoneNo;
        private String emailId;
        private String displayId;
        private String licenceNumber;
        private String licencePhoto;
        private boolean documentVerified;

        public DriverDTOBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public DriverDTOBuilder setRole(String role) {
            this.role = role;
            return this;
        }

        public DriverDTOBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public DriverDTOBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public DriverDTOBuilder setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
            return this;
        }

        public DriverDTOBuilder setEmailId(String emailId) {
            this.emailId = emailId;
            return this;
        }

        public DriverDTOBuilder setDisplayId(String displayId) {
            this.displayId = displayId;
            return this;
        }

        public DriverDTOBuilder setLicenceNumber(String licenceNumber) {
            this.licenceNumber = licenceNumber;
            return this;
        }

        public DriverDTOBuilder setLicencePhoto(String licencePhoto) {
            this.licencePhoto = licencePhoto;
            return this;
        }

        public DriverDTOBuilder setDocumentVerified(boolean documentVerified) {
            this.documentVerified = documentVerified;
            return this;
        }

        public DriverDTO build() {
            return new DriverDTO(this);
        }
    }

    @Override
    public String toString() {
        return "DriverDTO{" +
                "userId=" + userId +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", displayId='" + displayId + '\'' +
                ", licenceNumber='" + licenceNumber + '\'' +
                ", licencePhoto='" + licencePhoto + '\'' +
                ", documentVerified=" + documentVerified +
                '}';
    }
}