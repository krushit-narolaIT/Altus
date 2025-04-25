ALTER TABLE rides
    ADD COLUMN cancellation_charge         DECIMAL(10, 2) DEFAULT 0.00,
    ADD COLUMN cancellation_driver_earning DECIMAL(10, 2) DEFAULT 0.00,
    ADD COLUMN cancellation_system_earning DECIMAL(10, 2) DEFAULT 0.00,
    ADD COLUMN driver_penalty              DECIMAL(10, 2) DEFAULT 0.00;
