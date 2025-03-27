ALTER TABLE users
    ADD COLUMN is_blocked BOOLEAN DEFAULT FALSE;
ALTER TABLE users
    ADD COLUMN total_ratings INT DEFAULT 0;
ALTER TABLE users
    ADD COLUMN rating_count INT DEFAULT 0;

CREATE TABLE feedback
(
    feedback_id  INT AUTO_INCREMENT PRIMARY KEY,
    from_user_id INT NOT NULL,
    to_user_id   INT NOT NULL,
    ride_id      INT NOT NULL,
    rating       INT NOT NULL,
    comment      TEXT,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_user_id) REFERENCES users (user_id),
    FOREIGN KEY (to_user_id) REFERENCES users (user_id),
    FOREIGN KEY (ride_id) REFERENCES rides (ride_id) ON DELETE CASCADE,
    CHECK (rating BETWEEN 0 AND 5)
);

ALTER TABLE feedback DROP CHECK rating;

ALTER TABLE feedback ADD CONSTRAINT chk_rating CHECK (rating BETWEEN 1 AND 5);


