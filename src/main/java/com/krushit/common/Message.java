package com.krushit.common;

public final class Message {

    private Message() {
    }

    public static final String DRIVER_NOT_EXIST = "Driver Not Exist";
    public static final String GENERIC_ERROR = "Oops something went wong. please try after sometime or contact our support team.";
    public static final String USER_ALREADY_EXIST = "User Already Exist";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String REGISTRATION_SUCCESSFUL = "Registration Successful";
    public static final String CHOOSE_STRONG_PASSWORD = "CHOOSE STRONG PASSWORD";
    public static final String PLEASE_ENTER_VALID_PASSWORD = "Password must contain at least one uppercase letter, one special character, and one digit.";
    public static final String PLEASE_ENTER_VALID_PHONE_NO = "Please enter a valid phone number.";
    public static final String PLEASE_ENTER_VALID_EMAIL = "Please enter a valid email.";
    public static final String USER_REGISTERED_SUCCESSFULLY = "User Registration Successful";
    public static final String INVALID_CONTENT_TYPE = "Invalid Content Type";
    public static final String EMAIL_AND_PASS_REQUIRED = "Email & Password are Required";
    public static final String INVALID_EMAIL_AND_PASS = "Invalid Email & Password";
    public static final String LOGIN_SUCCESSFUL = "Login Successful";
    public static final String DRIVER_REGISTERED_SUCCESSFULLY = "Driver Registration Successful";
    public static final String DRIVER_ALREADY_EXIST = "Driver Already Exist";
    public static final String DATABASE_ERROR = "Database Error";
    public static final String DATABASE_CONNECTION_ESTABLISHED = "Database Connection Successful";
    public static final String DATABASE_CONNECTION_FAILED = "Database Connection Failed";
    public static final String USER_REGISTRATION_FAILED = "User registration failed.";
    public static final String APPLICATION_JSON = "application/json";
    public static final String UTF_8 = "UTF-8";
    public static final String EMPTY_PASSWORD = "Please fill the Password";
    public static final String EMPTY_EMAIL = "Please enter email";
    public static final String UNAUTHORIZED = "You don't have valid privilege to perform this operation";

    public static final class Driver {
        private Driver() {
        }

        public static final String DRIVER_NOT_EXIST = "Driver Not Exist";
        public static final String DRIVER_PATH = "/DriverSignUp";
        public static final String NO_DRIVERS_FOUND = "No Drivers Found";
        public static final String FAILED_TO_RETRIEVE_DRIVERS = "Failed to retrieve driver";
        public static final String SUCCESSFULLY_RETRIEVED_DRIVERS = "Successfully Retrieved Driver";
        public static final String VERIFICATION_DONE_SUCCESSFUL = "Driver verification updated successfully!";
        public static final String REGISTERED_SUCCESSFUL = "Driver Registered Successful";
        public static final String DOCUMENT_STORED_SUCCESSFULLY = "Document Stored Successfully";
        public static final String DOCUMENT_NOT_UPLOADED = "Driver document not uploaded";
        public static final String FAILED_TO_INSERT_DRIVER_DETAIL = "Failed to insert driver detail";
        public static final String NO_PENDING_VERIFICATION = "No pending driver verifications.";
        public static final String FAILED_TO_RETRIEVE_DRIVER = "Error retrieving driver verifications.";
        public static final String SUCCESSFULLY_RETRIEVE_DRIVERS = "Pending driver verifications retrieved successfully.";
    }

    public static final class Customer {
        private Customer() {
        }

        public static final String CUSTOMER_PATH = "/UserSignUp";
        public static final String NO_CUSTOMER_FOUND = "No Customers Found";
        public static final String FAILED_TO_RETRIEVE_CUSTOMER = "Failed to retrieve customers";
        public static final String SUCCESSFULLY_RETRIEVED_CUSTOMER = "Successfully Retrieved Customers";
        public static final String REGISTERED_SUCCESSFUL = "Driver Registered Successful";
        public static final String DOCUMENT_STORED_SUCCESSFULLY = "Document Stored Successfully";
        public static final String FAILED_TO_INSERT_DRIVER_DETAIL = "Failed to insert driver detail";
        public static final String NO_PENDING_VERIFICATION = "No pending driver verifications.";
        public static final String FAILED_TO_RETRIEVE_DRIVER = "Error retrieving driver verifications.";
        public static final String SUCCESSFULLY_RETRIEVE_DRIVERS = "Pending driver verifications retrieved successfully.";
    }

    public static final class Vehicle {

        private Vehicle() {
        }

        public static final String INVALID_VEHICLE_TYPE = "Please Enter Valid Vehicle Type";
        public static String ERROR_OCCUR_WHILE_ADDING_SERVICE = "Error Occur While Adding Vehicle Service";
        public static String ERROR_OCCUR_WHILE_ADDING_MODEL = "Error Occur While Adding Vehicle Model";
    }

    public static final class Database {

        private Database() {
        }

        public static final String INTERNAL_DB_ERROR = "Internal Database Error";
    }
}
