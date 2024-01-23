CREATE TABLE cart
(
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    user_id BIGINT,
    CONSTRAINT fk_cart_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE table cart_products
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    CONSTRAINT fk_cart_cart_id FOREIGN KEY (cart_id) REFERENCES cart(id),
    CONSTRAINT fk_product_product_id FOREIGN KEY (product_id) REFERENCES product(id)
);