INSERT INTO category(name)
VALUES ('Electronics'),
       ('Clothing'),
       ('Appliances'),
       ('Furniture'),
       ('Books'),
       ('Home Decor'),
       ('Sports Equipment');


INSERT INTO brand(name)
VALUES ('Samsung'),
       ('Apple'),
       ('Sony'),
       ('LG'),
       ('Adidas'),
       ('Nike'),
       ('Whirlpool'),
       ('Canon'),
       ('Dell'),
       ('Dyson');

INSERT INTO product(name, description, price, category_id, brand_id, added)
VALUES
    -- Electronics
    ('Samsung Galaxy Note 20', 'Smartphone', 1499.00, 1, 1, now() - interval '5 days'),
    ('Apple MacBook Pro', 'Laptop', 2399.00, 1, 2, now() - interval '2 days'),
    ('Sony 65-inch 4K OLED TV', 'Smart TV', 2999.00, 3, 3, now() - interval '3 days'),
    ('Sony WH-1000XM4 Headphones', 'Wireless Noise-Canceling Headphones', 349.99, 3, 3,
     now() - interval '1 day'),
    ('Apple Watch Series 6', 'Smartwatch', 399.00, 1, 2, now() - interval '4 days'),


    -- Clothing
    ('Adidas Originals Hoodie', 'Sports Apparel', 69.99, 2, 5, now() - interval '1 day'),
    ('Nike Dri-FIT Running Shorts', 'Athletic Wear', 29.99, 2, 6, now() - interval '4 days'),
    ('Nike Tech Fleece Joggers', 'Athletic Pants', 79.99, 2, 6, now() - interval '2 days'),
    ('Adidas Ultraboost Running Shoes', 'Running Sneakers', 179.99, 2, 5, now() - interval '3 days'),

    -- Appliances
    ('Whirlpool Front Load Washer', 'Washing Machine', 799.00, 3, 7, now() - interval '2 days'),
    ('LG French Door Refrigerator', 'Large Refrigerator', 2499.00, 3, 4, now() - interval '6 days'),
    ('Dyson V11 Cordless Vacuum', 'Cordless Vacuum Cleaner', 499.00, 3, 10, now() - interval '5 days'),

    -- Furniture

    -- Books

    -- Home Decor

    -- Sports Equipment
    ('Basketball', 'Sports Ball', 24.99, 7, 6, now() - interval '3 days'),
    ('Yoga Mat', 'Exercise Equipment', 29.99, 7, 5, now() - interval '2 days'),
    ('Tennis Racket', 'Sports Equipment', 89.99, 7, 6, now() - interval '2 days'),
    ('Adjustable Dumbbell Set', 'Weightlifting Equipment', 199.99, 7, 6, now() - interval '1 day');

INSERT INTO user_role(name, description)
VALUES ('ADMIN', 'Full authorities'),
       ('CUSTOMER', 'Customer authorities');

INSERT INTO users(first_name, last_name, email, password, role_id, active)
VALUES ('Janusz', 'Kowalski', 'janusz.kowalski@test.pl', '{noop}password', 1, true),
       ('Sebastian', 'Kowalski', 'sebastian.kowalski@test.pl', '{noop}password', 2, true),
       ('Jan', 'Kowalski', 'jan.kowalski@test.pl', '{noop}password', 2, false);

INSERT INTO warehouse (product_id, quantity, available)
VALUES (1, 23, true),
       (2, 24, true),
       (3, 10, true),
       (4, 14, false),
       (5, 8, true),
       (6, 16, true),
       (7, 12, true),
       (8, 3, true),
       (9, 2, true),
       (10, 5, false);

INSERT INTO shipping (name, shipping_cost)
VALUES ('STANDARD', 10.99),
       ('EXPRESS', 19.99),
       ('PERSONAL_COLLECTION', 0);

INSERT INTO address(postal_code, street, building_number, phone_number)
VALUES ('22-400', 'Orzeszkowej', '17b', '666666666'),
       ('11-222', 'Wrocławska', '20', '666996669'),
       ('50-400', 'Grunwaldzka', '12', '554455778');

INSERT INTO cart(user_id, used)
VALUES (1, true),
       (2, true),
       (2, false);

INSERT INTO cart_products(cart_id, product_id)
VALUES (1, 1),
       (1, 1),
       (1, 3),
       (2, 5),
       (2, 6),
       (2, 8),
       (3, 7),
       (3, 7);

INSERT INTO orders(status, user_id, cart_id, shipping_id, address_id, order_value, created)
VALUES ('NEW', 1, 1, 1, 1, 5977.00, '2024-01-08 14:10:06'),
       ('PAID', 2, 2, 2, 2, 548.98, '2024-01-16 18:05:06');

INSERT INTO review(user_id, rating, comment, product_id, added)
VALUES (1, 4, 'long comment to test product(id: 1) by user 1', 1, now()),
       (2, 5, 'long comment to test product(id: 1) by user 2', 1, now());

-- test data for statistics
INSERT INTO cart(user_id, used)
VALUES (2, true),
       (2, true),
       (2, true);

INSERT INTO cart_products(cart_id, product_id)
VALUES (4, 2),
       (4, 3),
       (5, 1),
       (5, 5),
       (5, 5),
       (5, 5),
       (5, 3),
       (5, 6),
       (5, 7),
       (5, 5),
       (6, 6),
       (6, 8),
       (6, 7),
       (6, 7),
       (6, 9),
       (6, 2),
       (6, 3);

INSERT INTO orders(status, user_id, cart_id, shipping_id, address_id, order_value, created)
VALUES ('RECEIVED', 2, 4, 3, 2, 5977.00, NOW() - INTERVAL '1' MONTH),
       ('DELIVERED', 2, 5, 2, 2, 548.98, NOW() - INTERVAL '1' MONTH),
       ('DELIVERED', 2, 6, 2, 2, 548.98, NOW() - INTERVAL '1' MONTH);

INSERT INTO promo (code, discount_percent, active_start, active_end, active, usage_count, max_usage_count)
VALUES
    ('CODE1', 5, '2024-02-20 08:00:00', '2024-02-21 08:00:00', FALSE, 50, 50),
    ('CODE2', 10, '2024-02-21 08:00:00', '2024-02-22 08:00:00', TRUE, 0, 100),
    ('CODE3', 15, '2024-02-22 08:00:00', '2024-02-23 08:00:00', TRUE, 0, 100),
    ('CODE4', 20, '2024-02-23 08:00:00', '2024-02-24 08:00:00', TRUE, 0, 100),
    ('CODE5', 25, '2024-02-24 08:00:00', '2024-02-25 08:00:00', TRUE, 0, 100),
    ('CODE6', 30, '2024-02-25 08:00:00', '2024-02-26 08:00:00', FALSE, 100, 100),
    ('CODE7', 35, '2024-02-26 08:00:00', '2024-02-27 08:00:00', TRUE, 99, 100),
    ('CODE8', 5, '2024-02-27 08:00:00', '2024-02-28 08:00:00', TRUE, 0, 100),
    ('CODE9', 10, '2024-02-28 08:00:00', '2024-02-29 08:00:00', TRUE, 0, 100),
    ('CODE10', 15, '2024-02-29 08:00:00', '2024-03-01 08:00:00', TRUE, 0, 100),
    ('CODE11', 20, '2024-03-01 08:00:00', '2024-03-02 08:00:00', TRUE, 0, 100),
    ('CODE12', 25, '2024-03-02 08:00:00', '2024-03-03 08:00:00', TRUE, 14, 20),
    ('CODE13', 30, '2024-03-03 08:00:00', '2024-03-04 08:00:00', TRUE, 23, 100),
    ('CODE14', 35, '2024-03-04 08:00:00', '2024-03-05 08:00:00', TRUE, 21, 100),
    ('CODE15', 5, '2024-03-05 08:00:00', '2024-03-06 08:00:00', TRUE, 66, 100),
    ('CODE16', 10, '2024-03-06 08:00:00', '2024-03-07 08:00:00', TRUE, 22, 50),
    ('CODE17', 15, '2024-03-07 08:00:00', '2024-03-08 08:00:00', FALSE, 100, 100),
    ('CODE18', 20, '2024-03-08 08:00:00', '2024-03-09 08:00:00', TRUE, 0, 100),
    ('CODE19', 25, '2024-03-09 08:00:00', '2024-03-10 08:00:00', FALSE, 30, 30),
    ('CODE20', 30, '2024-03-10 08:00:00', '2024-03-11 08:00:00', FALSE, 20, 20);

INSERT INTO user_role(name, description)
VALUES ('EMPLOYEE', 'Employee authorities');

INSERT INTO product (name, description, price, category_id, brand_id, added, promotion_price)
VALUES
    ('Product 17', 'Description for Product 17', 100.00, 1, 1, '2024-03-10', 90.00),
    ('Product 18', 'Description for Product 18', 150.00, 1, 2, '2024-03-11', 140.00),
    ('Product 19', 'Description for Product 19', 200.00, 2, 1, '2024-03-12', 190.00),
    ('Product 20', 'Description for Product 20', 120.00, 2, 2, '2024-03-13', 110.00),
    ('Product 21', 'Description for Product 21', 180.00, 1, 1, '2024-03-14', 170.00),
    ('Product 22', 'Description for Product 22', 220.00, 1, 2, '2024-03-15', 210.00),
    ('Product 23', 'Description for Product 23', 130.00, 2, 1, '2024-03-16', 120.00),
    ('Product 24', 'Description for Product 24', 160.00, 2, 2, '2024-03-17', 150.00),
    ('Product 25', 'Description for Product 25', 190.00, 1, 1, '2024-03-18', 180.00),
    ('Product 26', 'Description for Product 26', 210.00, 1, 2, '2024-03-19', 200.00),
    ('Product 27', 'Description for Product 27', 200.00, 1, 2, '2024-03-19', 199.00),
    ('Product 28', 'Description for Product 28', 200.00, 1, 2, '2024-03-19', 199.00);

INSERT INTO warehouse (product_id, quantity, available)
VALUES (11, 23, true),
       (12, 24, true),
       (13, 10, true),
       (14, 14, false),
       (15, 8, true),
       (16, 16, true),
       (17, 12, true),
       (18, 3, true),
       (19, 2, true),
       (20, 5, false),
       (21, 24, true),
       (22, 10, true),
       (23, 14, false),
       (24, 8, true),
       (25, 16, true),
       (26, 12, true),
       (27, 2, true);