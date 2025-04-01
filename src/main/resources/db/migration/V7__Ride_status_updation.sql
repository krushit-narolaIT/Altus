SET GLOBAL event_scheduler = ON;


DELIMITER $$
CREATE EVENT update_completed_rides
    ON SCHEDULE EVERY 1 HOUR
    DO
    BEGIN
        UPDATE rides
        SET ride_status = 'Completed'
        WHERE ride_status IN ('Scheduled', 'Ongoing')
          AND TIMESTAMP(ride_date, pick_up_time) + INTERVAL 30 MINUTE <= NOW();

        UPDATE drivers
        SET is_available = 1
        WHERE driver_id IN (
            SELECT DISTINCT driver_id
            FROM rides
            WHERE ride_status = 'Completed'
              AND TIMESTAMP(ride_date, pick_up_time) + INTERVAL 30 MINUTE <= NOW()
        );
    END $$
DELIMITER ;


DELIMITER $$

CREATE EVENT update_driver_status_before_ride
    ON SCHEDULE EVERY 1 HOUR
    DO
    BEGIN
        UPDATE drivers
        SET is_available = 0
        WHERE driver_id IN (
            SELECT DISTINCT driver_id
            FROM rides
            WHERE ride_status = 'Scheduled'
              AND TIMESTAMP(ride_date, pick_up_time) - INTERVAL 30 MINUTE <= NOW()
        );
    END $$

DELIMITER ;
