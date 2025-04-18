CREATE TABLE commission_slabs
(
    commission_id         INT PRIMARY KEY AUTO_INCREMENT,
    from_km               DOUBLE        NOT NULL,
    to_km                 DOUBLE        NOT NULL,
    commission_percentage DECIMAL(3, 1) NOT NULL
);

INSERT INTO commission_slabs (from_km, to_km, commission_percentage)
VALUES (0.00, 3.00, 25.0),
       (3.01, 10.00, 20.0),
       (10.01, 20.00, 15.0),
       (20.01, 50.00, 12.0),
       (50.01, 500.00, 10.0);

CREATE TABLE rides
(
    ride_id               INT PRIMARY KEY AUTO_INCREMENT,
    ride_status           ENUM ('Scheduled', 'Ongoing', 'Completed', 'Cancelled', 'Rejected') NOT NULL,
    pick_location_id      INT                                                                 NOT NULL,
    drop_off_location_id  INT                                                                 NOT NULL,
    customer_id           INT                                                                 NOT NULL,
    driver_id             INT                                                                 NOT NULL,
    ride_date             DATE                                                                NOT NULL,
    pick_up_time          TIME                                                                NOT NULL,
    drop_off_time         TIME,
    display_id            VARCHAR(10)                                                         NOT NULL,
    total_km              DOUBLE                                                              NOT NULL,
    total_cost            DECIMAL(10, 2)                                                      NOT NULL,
    created_at            DATETIME                                                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            DATETIME                                                                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    payment_mode          ENUM ('Cash', 'Card', 'UPI')                                        NOT NULL,
    payment_status        VARCHAR(10)                                                         NOT NULL,
    commission_percentage DECIMAL(3, 1)                                                       NOT NULL,
    driver_earning        DECIMAL(10, 2)                                                      NOT NULL,
    system_earning        DECIMAL(10, 2)                                                      NOT NULL,
    FOREIGN KEY (pick_location_id) REFERENCES locations (location_id),
    FOREIGN KEY (drop_off_location_id) REFERENCES locations (location_id),
    FOREIGN KEY (customer_id) REFERENCES users (user_id),
    FOREIGN KEY (driver_id) REFERENCES users (user_id)
);