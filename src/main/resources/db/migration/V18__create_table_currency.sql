CREATE TABLE currency(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    code VARCHAR(3),
    effective_date DATE,
    ask NUMERIC(38,2),
    bid NUMERIC(38,2)
);