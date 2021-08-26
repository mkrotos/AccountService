CREATE TABLE account (
    id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    balance DECIMAL NOT NULL,
    currency VARCHAR,
    PRIMARY KEY (id)
);