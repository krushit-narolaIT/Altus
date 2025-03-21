package com.krushit.common;

public final class Message {
    public static final String SESSION_EXPIRED = "Session Expired";
    public static final String UNAUTHORIZED_ACCESS = "Y";

    private Message() {
    }

    public static final class Ride {
        public static final String LOCATION_IS_REQUIRE = "Please enter source & destination location";
        public static final String RIDE_SERVICES_FETCHED_SUCCESSFULLY = "Available ride services fetched successfully.";
        public static final String PLEASE_ENTER_VALID_LOCATION = "Please Enter Valid Location Id";
        public static final String INVALID_GRAPH_HOPPER_API_RESPONSE = "Graphhopper api response is invalid";
        public static final String COORDINATES_NOT_FOUND_FOR = "Coordinates not found for location: ";

        private Ride(){
        }
    }
    public static final class User {
        public static final String INVALID_OPERATION = "Please log in first";
        public static final String USER_LOGOUT_SUCCESSFULLY = "Logout successfully";
        public static final String ERROR_WHILE_REGISTERING_USER = "Error occur while registering user";
        public static final String PLEASE_ENTER_VALID_EMAIL_OR_PASS = "Please enter valid email & password";
        public static final String ERROR_WHILE_USER_LOGIN = "Error while user login";
        public static final String ERROR_WHILE_VALIDATING_USER = "Error while validating user";
        public static final String ERROR_WHILE_CHECKING_USER_EXISTENCE = "Error while checking user existence";
        public static final String ERROR_WHILE_GET_USER_DETAILS = "Error while getting user details";
        public static final String ERROR_WHILE_GETTING_ALL_CUSTOMERS = "Error while getting all customers";

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
        public static final String SESSION_EXPIRED = "Session Expired";

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
        public static final String ERROR_WHILE_UPDATING_DRIVER_AVAILABILITY = "Error occur while updating driver availability";
        public static final String ERROR_WHILE_CHECKING_LICENCE_NUMBER = "Error while checking licence number";
        public static final String ERROR_FOR_CHECKING_DRIVER_DOCUMENT_UPLOADED = "Error occur while checking driver document";
        public static final String ERROR_FOR_CHECKING_DRIVER_DOCUMENT_VERIFIED = "Error occur while checking driver document verification";
        public static final String ERROR_FOR_GETTING_DRIVER_ID_FROM_USER_ID = "Error while getting driver id from user id";
        public static final String ERROR_WHILE_FETCHING_ALL_DRIVERS = "Error while fetching all drivers";
        public static final String PLEASE_PERFORM_VALID_VERIFICATION_OPERATION = "Please enter valid approval request : only [ACCEPT : REJECT] accepted";
        public static final String ERROR_WHILE_VERIFYING_DRIVER = "Error occur while verification of driver";
        public static final String ERROR_WHILE_CHECKING_DRIVER_EXISTENCE = "Error occur while checking driver existence";
        public static final String ERROR_WHILE_GETTING_PENDING_VERIFICATION_DRIVER = "Error while getting pending driver verification";
        public static final String ERROR_WHILE_INSERT_DRIVER_DETAILS = "Error while inserting driver details";

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
        public static final String BRAND_MODEL_ALREADY_EXISTS = "This model of this brand already exist";
        public static final String SUCCESSFULLY_RETRIVED_ALL_BRAND_MODELS = "Successfully retrieve all brand models";
        public static final String ERROR_OCCUR_WHILE_CHECKING_BRAND_MODEL = "Error occur while checking brand model";
        public static final String ERROR_OCCUR_WHILE_CHECKING_MIN_YEAR = "Error occur while checking min year for brand model";
        public static final String BRAND_MODEL_NOT_SUPPORTED = "Sorry, Your vehicle brand model not supported by system";
        public static final String PENDING = "Pending";

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
        public static final String FLYWAY_SUCCESSFUL = "Flyway set up successful";
        public static final String FLYWAY_FAILED = "Failed to set up flyway";
        public static final String DRIVER_NOT_FOUND = "Driver not found";

        private Database() {
        }

        public static final String DATABASE_ERROR = "Database error occurred while logging in.";
        public static final String INTERNAL_DB_ERROR = "Internal Database Error";
        public static final String CONNECTION_SUCCESSFUL = "Database connection successful";
    }

    public static final class Location {
        public static final String LOCATION_ADDED_SUCCESSFULLY = "Location Added Successfully";
        public static final String LOCATIONS_NOT_FOUND = "Locations not found";
        public static final String SUCCESSFULLY_RETRIEVED_ALL_LOCATIONS = "Successfully retrieved locations";
        public static final String LOCATION_NOT_FOUND = "Location not found for deletion";
        public static final String LOCATION_DELETED_SUCCESSFULLY = "Location deleted successfully";

        private Location(){
        }
    }


}
