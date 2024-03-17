CREATE TABLE products_images
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    image_file_data_id BIGINT NOT NULL,
    CONSTRAINT fk_product_product_id FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_image_file_data_image_id FOREIGN KEY (image_file_data_id) REFERENCES image_file_data(id)
)