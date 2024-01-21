CREATE TABLE warehouse
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    product_id BIGINT UNIQUE,
    quantity BIGINT,
    available BOOLEAN,
    CONSTRAINT fk_warehouse_product_id FOREIGN KEY (product_id) REFERENCES product(id)

)