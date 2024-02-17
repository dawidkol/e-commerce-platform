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
       ('CUSTOMER', 'Basic authorities');

INSERT INTO users(first_name, last_name, email, password, role_id)
VALUES ('Janusz', 'Kowalski', 'janusz.kowalski@test.pl', '{noop}password', 1),
       ('Sebastian', 'Kowalski', 'sebastian.kowalski@test.pl', '{noop}password', 2);

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
VALUES ('RECEIVED', 2, 4, 3, 2, 5977.00, '2024-01-08 14:10:06'),
       ('DELIVERED', 2, 5, 2, 2, 548.98, '2024-01-16 18:05:06'),
       ('DELIVERED', 2, 6, 2, 2, 548.98, '2024-01-16 18:05:06');