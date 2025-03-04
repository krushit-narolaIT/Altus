package com.krushit.entity;

import java.sql.Timestamp;

public class User {
    private  int userId;
    private  int roleId;
    private  String firstName;
    private  String lastName;
    private  String phoneNo;
    private  String emailId;
    private  String password;
    private  boolean isActive;
    private  String displayId;
    private  Timestamp createdAt;
    private  Timestamp updatedAt;
    private  String createdBy;
    private  String updatedBy;

    public User() {
    }

    public User(int userId, int roleId, String firstName, String lastName, String phoneNo, String emailId, String password, boolean isActive, String displayId, Timestamp createdAt, Timestamp updatedAt, String createdBy, String updatedBy) {
        this.userId = userId;
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.password = password;
        this.isActive = isActive;
        this.displayId = displayId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public User(int roleId, String firstName, String lastName, String phoneNo, String emailId, String password) {
        this.roleId = roleId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.password = password;
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
