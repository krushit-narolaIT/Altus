create schema altus;

use altus;

CREATE TABLE roles
(
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(10) NOT NULL
);

INSERT INTO roles
VALUES (1, 'Admin'),
       (2, 'Customer'),
       (3, 'Driver');

CREATE TABLE users
(
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    role_id    INT                 NOT NULL,
    first_name VARCHAR(50)         NOT NULL,
    last_name  VARCHAR(50)         NOT NULL,
    phone_no   VARCHAR(10) UNIQUE  NOT NULL,
    email_id   VARCHAR(254) UNIQUE NOT NULL,
    password   VARCHAR(20)         NOT NULL,
    is_active  BOOLEAN             NOT NULL DEFAULT FALSE,
    display_id VARCHAR(10) UNIQUE,
    created_at DATETIME                     DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
);

INSERT INTO users (role_id, first_name, last_name, phone_no, email_id, password, is_active, display_id)
VALUES (1, 'Krushit', 'Babariya', '7777777777', 'ksb@admin.in', 'sadmin@123', TRUE, 'SUPERADMIN');

CREATE TABLE drivers
(
    driver_id            INT AUTO_INCREMENT PRIMARY KEY,
    user_id              INT UNIQUE  NOT NULL,
    licence_number       VARCHAR(15) UNIQUE,
    is_document_verified BOOLEAN              DEFAULT FALSE,
    licence_photo        VARCHAR(255),
    is_available         BOOLEAN,
    verification_status  ENUM ('Pending', 'Rejected', 'Verified', 'Incomplete') NOT NULL DEFAULT 'Incomplete',
    comment              VARCHAR(254),
    created_at           DATETIME             DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT unique_driver_license UNIQUE (licence_number)
);

CREATE TABLE vehicle_Service
(
    service_id            INT AUTO_INCREMENT PRIMARY KEY,
    service_name          VARCHAR(50) UNIQUE      NOT NULL,
    base_fare             DECIMAL(10, 2)          NOT NULL,
    per_km_rate           DECIMAL(10, 2)          NOT NULL,
    vehicle_type          ENUM ('2W', '3W', '4W') NOT NULL,
    max_passengers        INT                     NOT NULL,
    commission_percentage DECIMAL(3, 1)           NOT NULL
);

CREATE TABLE brand_Models
(
    brand_model_id INT AUTO_INCREMENT PRIMARY KEY,
    service_id     INT         NOT NULL,
    brand_name     VARCHAR(20) NOT NULL,
    model          VARCHAR(20) NOT NULL,
    min_year       INT    NOT NULL,
    FOREIGN KEY (service_id) REFERENCES Vehicle_Service (service_id) ON DELETE CASCADE
);

CREATE TABLE vehicles
(
    vehicle_id           INT AUTO_INCREMENT PRIMARY KEY,
    driver_id            INT UNIQUE,
    brand_model_id       INT                                             NOT NULL,
    registration_number  VARCHAR(20) UNIQUE                              NOT NULL,
    year                 INT                                             NOT NULL,
    fuel_type            ENUM ('Petrol', 'Diesel', 'Electric', 'Hybrid') NOT NULL,
    transmission         ENUM ('Manual', 'Automatic')                    NOT NULL,
    ground_clearance     DECIMAL(5, 2),
    wheel_base           DECIMAL(5, 2),
    FOREIGN KEY (driver_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (brand_model_id) REFERENCES brand_models (brand_model_id) ON DELETE CASCADE
);

CREATE TABLE locations
(
    location_id INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(20) UNIQUE NOT NULL
);