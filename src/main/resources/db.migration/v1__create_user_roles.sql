USE
altus;

CREATE TABLE roles
(
    role_id   INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(10) NOT NULL
);

INSERT INTO roles
VALUES (1, 'Admin'),
       (2, 'Customer');
INSERT INTO roles
VALUES (3, 'Driver');