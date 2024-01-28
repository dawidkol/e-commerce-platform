CREATE TABLE orders
(
    id      BIGSERIAL NOT NULL PRIMARY KEY,
    status VARCHAR(30),
    user_id BIGINT,
    cart_id BIGINT,
    shipping_id BIGINT,
    address_id BIGINT,
    order_value NUMERIC(38,2),
    created TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_orders_cart FOREIGN KEY (cart_id) REFERENCES cart (id),
    CONSTRAINT fk_orders_shipping FOREIGN KEY (shipping_id) REFERENCES shipping(id),
    CONSTRAINT fk_orders_address FOREIGN KEY (address_id) REFERENCES address(id)
);