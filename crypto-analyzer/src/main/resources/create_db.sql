DROP TABLE IF EXISTS crypto_currency;

CREATE TABLE IF NOT EXISTS crypto_currency (
            id BIGINT AUTO_INCREMENT PRIMARY KEY,
            currency_code VARCHAR(10) NOT NULL,
            price DECIMAL(18, 8) NOT NULL,
            timestamp BIGINT NOT NULL,
    CONSTRAINT uq_timestamp_currency UNIQUE(timestamp, currency_code)
);
