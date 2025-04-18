CREATE TABLE user_favorites
(
    user_id          INT NOT NULL,
    favorite_user_id INT NOT NULL,
    PRIMARY KEY (user_id, favorite_user_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (favorite_user_id) REFERENCES users (user_id)
);
