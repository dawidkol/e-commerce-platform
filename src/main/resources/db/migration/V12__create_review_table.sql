CREATE TABLE review
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT,
    rating INT,
    comment VARCHAR(30000),
    product_id BIGINT,
    added TIMESTAMP,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES product(id)
);