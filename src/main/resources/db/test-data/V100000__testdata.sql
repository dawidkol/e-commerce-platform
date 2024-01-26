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