CREATE TABLE IF NOT EXISTS table_definition (
    id      SERIAL PRIMARY KEY,
    table_id VARCHAR(10) NOT NULL UNIQUE,  -- UNIQUE enables ON CONFLICT
    capacity INT NOT NULL
);

CREATE TABLE IF NOT EXISTS table_availability (
    id               SERIAL PRIMARY KEY,
    customer_id      VARCHAR(255),
    capacity         INT NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    reservation_date DATE,
    reservation_time TIME
);