CREATE TABLE stripe_payment
(
    payment_intent VARCHAR(255),
    order_id BIGINT,
    refund BOOLEAN DEFAULT FALSE,
    refunded BOOLEAN DEFAULT FALSE
);