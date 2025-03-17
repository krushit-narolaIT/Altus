package com.krushit.common;

public final class Message {

    private Message() {
    }

    public static final class User {
        private User(){
        }
        public static final String PLEASE_ENTER_PASSWORD = "Please Enter Password";
        public static final String PLEASE_ENTER_PHONE_NO = "Please Enter Phone no";
        public static final String PLEASE_ENTER_EMAIL = "Please Enter Email";
        public static final String PLEASE_ENTER_FIRST_NAME = "Please Enter First Name";
        public static final String PLEASE_ENTER_LASE_NAME = "Please Enter Last Name";
        public static final String INVALID_USER_ROLE = "Please register with valid user roles";
        public static final String USER_NOT_FOUND = "User not exist";
        public static final String ENTER_VALID_USER_DATA = "Please Enter Valid User Data";
        public static final String PLEASE_ENTER_VALID_PASSWORD = "Password must contain at least one uppercase letter, one special character, and one digit.";
        public static final String PLEASE_ENTER_VALID_PHONE_NO = "Please enter a valid phone number.";
        public static final String PLEASE_ENTER_VALID_EMAIL = "Please enter a valid email.";
        public static final String USER_REGISTERED_SUCCESSFULLY = "User Registration Successful";
        public static final String EMAIL_AND_PASS_REQUIRED = "Email & Password are Required";
        public static final String INVALID_EMAIL_AND_PASS = "Invalid Email & Password";
        public static final String LOGIN_SUCCESSFUL = "Login Successful";
    }

    public static final class Auth {
        private Auth (){
        }
        public static final String PLEASE_LOGIN_FIRST = "Please login first";
        public static final String UNAUTHORIZED = "You don't have valid privilege to perform this operation";
    }

    public static final String INVALID_CONTENT_TYPE = "Invalid Content Type";
    public static final String DRIVER_NOT_EXIST = "Driver Not Exist";
    public static final String GENERIC_ERROR = "Oops something went wong. please try after sometime or contact our support team.";
    public static final String USER_ALREADY_EXIST = "User Already Exist";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String REGISTRATION_SUCCESSFUL = "Registration Successful";
    public static final String CHOOSE_STRONG_PASSWORD = "CHOOSE STRONG PASSWORD";
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
        public static final String LICENCE_NUMBER_IS_REQUIRED = "Please enter licence number";
        public static final String ENTER_VALID_LICENCE_NUMBER = "Please enter valid licence number";
        public static final String LICENCE_PHOTO_IS_REQUIRD = "Licence photo is required";
        public static final String UPLOAD_VALID_LICENCE_PHOTO = "Licence photo file does not exist";
        public static final String LICENCE_NUMBER_IS_ALREADY_EXIST = "Driver already registered with this licence number";
        public static final String LICENCE_PHOTO_PATH_IS_REQUIRD = "Licence photo not exist at : ";
        public static final String FAILED_TO_STORE_DOCUMENT = "Failed to store licence photo: ";
        public static final String DOCUMENT_NOT_VERIFIED = "Driver document not verified";
        public static final String INVALID_DRIVER_ID = "Please enter valid driver id";
        public static final String DOCUMENT_UPLOADING_FAILED = "Failed to upload driver document";
        public static final String DRIVER_NOT_EXIST = "Driver Not Exist";
        public static final String DRIVER_PATH = "/DriverSignUp";
        public static final String NO_DRIVERS_FOUND = "No drivers found in system";
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
        public static final String NO_CUSTOMER_FOUND = "No users found in system";
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

        public static final String VERIFICATION_PENDING = "Vehicle Verification is Pending";
        public static final String ERROR_OCCUR_WHILE_ADDING_VEHICLE = "Error Occur While Adding Vehicle";
        public static final String PLEASE_ENTER_VALID_BASE_FARE = "Please Enter Valid Base Fare";
        public static final String PLEASE_ENTER_VALID_PER_KM_RATE = "Please enter valid per km rate";
        public static final String VEHICLE_SERVICE_IS_REQUIRED = "Please Enter Vehicle Service";
        public static final String INVALID_PASSENGER_CAPACITY = "Passenger capacity with 1 to 8";
        public static final String PLEASE_ENTER_VALID_COMMISSION_PERCENTAGE = "Please Enter Valid Commission percentage";
        public static final String VEHICLE_SERVICE_ADDED_SUCCESSFULLY = "Service added successfully";
        public static final String VEHICLE_SERVICE_ALREADY_EXISTS = "Vehicle Service is already exist";
        public static final String BRAND_MODEL_ADDED_SUCCESSFULLY = "Brand Model Added Successfully";
        public static final String VEHICLE_REGISTERED_SUCCESSFULLY = "Vehicle Registered Successfully";
        public static final String PLEASE_ENTER_VALID_VERIFICATION_REQUEST = "Please enter valid verification request";
        public static final String PLEASE_ENTER_VALID_DRIVER_ID = "Please enter valid driver id";
        public static final String PLEASE_ENTER_VALID_VERIFICATION_STATUS = "Please enter valid verification status, Allowed : ACCEPT, REJECT";
        public static final String PLEASE_ENTER_REJECTION_MESSAGE = "Please rejection message";
        public static final String VEHICLE_DATA_MISSING = "Invalid request! Vehicle data is missing.";
        public static final String DRIVER_ID_INVALID = "Please enter valid driver id";
        public static final String BRAND_MODEL_ID_INVALID = "Please enter valid brand model id";
        public static final String REGISTRATION_NUMBER_REQUIRED = "Registration number is required.";
        public static final String REGISTRATION_NUMBER_INVALID = "Please enter valid registration number, example: GJ12AB1234.";
        public static final String YEAR_INVALID = "Invalid manufacturing year, between 1990 and current year.";
        public static final String FUEL_TYPE_INVALID = "Please enter valid fuel type. Allowed : Petrol, Diesel, CNG, Electric, Hybrid.";
        public static final String TRANSMISSION_TYPE_INVALID = "Please enter valid transmission type. Allowed : Manual, Automatic.";
        public static final String GROUND_CLEARANCE_INVALID = "Please enter valid ground clearance";
        public static final String WHEEL_BASE_INVALID = "Please enter valid wheel base";
        public static final String PLEASE_ENTER_VALID_BRAND_MODEL = "Please enter a valid brand model.";
        public static final String PLEASE_ENTER_VALID_SERVICE_ID = "Please enter a valid service ID.";
        public static final String BRAND_NAME_IS_REQUIRED = "Brand name is required.";
        public static final String MODEL_NAME_IS_REQUIRED = "Model name is required.";
        public static final String PLEASE_ENTER_VALID_MIN_YEAR = "Please enter a valid minimum year.";
        public static String INVALID_VEHICLE_TYPE = "Please Enter Valid Vehicle Type : Only 2W, 3W, 4W are allowed";
        public static String ERROR_OCCUR_WHILE_ADDING_SERVICE = "Error Occur While Adding Vehicle Service";
        public static String ERROR_OCCUR_WHILE_ADDING_MODEL = "Error Occur While Adding Vehicle Model";
        public static String ERROR_OCCUR_WHILE_CHECK_VEHICLE_EXISTENCE = "Error checking driver vehicle existence.";
        public static String DRIVER_VEHICLE_ALREADY_EXIST = "Driver vehicle already exist";
        public static String PLEASE_ENTER_VALID_VEHICLE_SERVICE = "Please enter valid vehicle service";
    }

    public static final class Database {
        private Database() {
        }

        public static final String DATABASE_ERROR = "Database error occurred while logging in.";
        public static final String INTERNAL_DB_ERROR = "Internal Database Error";
        public static final String CONNECTION_SUCCESSFUL = "Database connection successful";
    }

    public static final class Location {
        public static final String LOCATION_ADDED_SUCCESSFULLY = "Location Added Successfully";

        private Location(){
        }
    }


}
