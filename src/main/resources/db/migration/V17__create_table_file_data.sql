CREATE TABLE image_file_data
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    type VARCHAR(500) NOT NULL,
    file_patch VARCHAR(500) NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT image_file_data_product_id FOREIGN KEY (product_id) REFERENCES product(id)
);