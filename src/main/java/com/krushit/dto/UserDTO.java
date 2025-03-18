package com.krushit.dto;

import com.krushit.model.Role;

public class UserDTO {
    private final int userId;
    private final Role role;
    private final String firstName;
    private final String lastName;
    private final String phoneNo;
    private final String emailId;
    private final String displayId;

    private UserDTO(UserDTOBuilder builder) {
        this.userId = builder.userId;
        this.role = builder.role;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNo = builder.phoneNo;
        this.emailId = builder.emailId;
        this.displayId = builder.displayId;
    }

    public static class UserDTOBuilder {
        private int userId;
        private Role role;
        private String firstName;
        private String lastName;
        private String phoneNo;
        private String emailId;
        private String displayId;

        public UserDTOBuilder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public UserDTOBuilder setRole(Role role) {
            this.role = role;
            return this;
        }

        public UserDTOBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDTOBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDTOBuilder setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
            return this;
        }

        public UserDTOBuilder setEmailId(String emailId) {
            this.emailId = emailId;
            return this;
        }

        public UserDTOBuilder setDisplayId(String displayId) {
            this.displayId = displayId;
            return this;
        }

        public UserDTO build() {
            return new UserDTO(this);
        }
    }

    public int getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getDisplayId() {
        return displayId;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", displayId='" + displayId + '\'' +
                '}';
    }
}