CREATE table product
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(3000),
    quantity BIGINT,
    category_id BIGINT,
    brand_id BIGINT,
    available BOOLEAN,
    added DATE,
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES category(id),
    CONSTRAINT fk_brand_id FOREIGN KEY (brand_id) REFERENCES brand(id)
);