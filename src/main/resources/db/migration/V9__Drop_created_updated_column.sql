ALTER TABLE drivers
    DROP COLUMN created_by,
    DROP COLUMN updated_by;

ALTER TABLE users
    DROP COLUMN created_by,
    DROP COLUMN updated_by;
