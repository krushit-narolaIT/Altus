package com.krushit.entity;

import java.sql.Timestamp;

public class UserBuilder {
    private final int userId;
    private final int roleId;
    private final String firstName;
    private final String lastName;
    private final String phoneNo;
    private final String emailId;
    private final String password;
    private final boolean isActive;
    private final String displayId;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private final String createdBy;
    private final String updatedBy;

    private UserBuilder(UserBuilderBuilder builder) {
        this.userId = builder.userId;
        this.roleId = builder.roleId;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNo = builder.phoneNo;
        this.emailId = builder.emailId;
        this.password = builder.password;
        this.isActive = builder.isActive;
        this.displayId = builder.displayId;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.createdBy = builder.createdBy;
        this.updatedBy = builder.updatedBy;
    }

    public static class UserBuilderBuilder {
        private int userId;
        private final int roleId;
        private final String firstName;
        private final String lastName;
        private final String phoneNo;
        private final String emailId;
        private final String password;
        private boolean isActive;
        private String displayId;
        private Timestamp createdAt;
        private Timestamp updatedAt;
        private String createdBy;
        private String updatedBy;

        public UserBuilderBuilder(int roleId, String firstName, String lastName, String phoneNo, String emailId, String password) {
            this.roleId = roleId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phoneNo = phoneNo;
            this.emailId = emailId;
            this.password = password;
        }

        public UserBuilderBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        public UserBuilderBuilder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public UserBuilderBuilder displayId(String displayId) {
            this.displayId = displayId;
            return this;
        }

        public UserBuilderBuilder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public UserBuilderBuilder updatedAt(Timestamp updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserBuilderBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public UserBuilderBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public com.krushit.entity.UserBuilder build() {
            return new com.krushit.entity.UserBuilder(this);
        }
    }

    // Getters
    public int getUserId() { return userId; }
    public int getRoleId() { return roleId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNo() { return phoneNo; }
    public String getEmailId() { return emailId; }
    public String getPassword() { return password; }
    public boolean isActive() { return isActive; }
    public String getDisplayId() { return displayId; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", isActive=" + isActive +
                ", displayId='" + displayId + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }
}
