CREATE TABLE shipping
(
    id   BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    shipping_cost NUMERIC(38,2)
);

CREATE TABLE address
(
    id              BIGSERIAL NOT NULL PRIMARY KEY,
    postal_code     VARCHAR(6),
    street          VARCHAR(100),
    building_number VARCHAR(15),
    phone_number    VARCHAR(20)
)