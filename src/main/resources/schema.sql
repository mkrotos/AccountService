CREATE TABLE account (
    id INTEGER NOT NULL AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    balance DECIMAL NOT NULL,
    currency VARCHAR,
    PRIMARY KEY (id)
);

CREATE TABLE exchange_rate (
    id INTEGER NOT NULL AUTO_INCREMENT,
    base_currency VARCHAR NOT NULL,
    target_currency VARCHAR NOT NULL,
    average_rate DECIMAL NOT NULL,
    date DATETIME2 NOT NULL,
    PRIMARY KEY (id)
)