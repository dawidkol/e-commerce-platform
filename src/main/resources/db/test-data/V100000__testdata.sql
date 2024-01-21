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

INSERT INTO product(name, description, price, quantity, category_id, brand_id, available, added)
VALUES
    -- Electronics
    ('Samsung Galaxy Note 20', 'Smartphone', 1499.00, 18, 1, 1, true, now() - interval '5 days'),
    ('Apple MacBook Pro', 'Laptop', 2399.00, 10, 1, 2, true, now() - interval '2 days'),
    ('Sony 65-inch 4K OLED TV', 'Smart TV', 2999.00, 7, 1, 3, false, now() - interval '3 days'),
    ('Sony WH-1000XM4 Headphones', 'Wireless Noise-Canceling Headphones', 349.99, 15, 1, 3, true,
     now() - interval '1 day'),
    ('Apple Watch Series 6', 'Smartwatch', 399.00, 20, 1, 2, true, now() - interval '4 days'),


-- Clothing
    ('Adidas Originals Hoodie', 'Sports Apparel', 69.99, 25, 2, 5, true, now() - interval '1 day'),
    ('Nike Dri-FIT Running Shorts', 'Athletic Wear', 29.99, 30, 2, 6, true, now() - interval '4 days'),
    ('Nike Tech Fleece Joggers', 'Athletic Pants', 79.99, 18, 2, 6, true, now() - interval '2 days'),
    ('Adidas Ultraboost Running Shoes', 'Running Sneakers', 179.99, 15, 2, 5, true, now() - interval '3 days'),

    -- Appliances
    ('Whirlpool Front Load Washer', 'Washing Machine', 799.00, 15, 3, 7, true, now() - interval '2 days'),
    ('LG French Door Refrigerator', 'Large Refrigerator', 2499.00, 8, 3, 4, false, now() - interval '6 days'),
    ('Dyson V11 Cordless Vacuum', 'Cordless Vacuum Cleaner', 499.00, 10, 3, 10, true, now() - interval '5 days'),

    -- Furniture

    -- Books

    -- Home Decor

    -- Sports Equipment
    ('Basketball', 'Sports Ball', 24.99, 10, 7, 6, true, now() - interval '3 days'),
    ('Yoga Mat', 'Exercise Equipment', 29.99, 15, 7, 5, true, now() - interval '2 days'),
    ('Tennis Racket', 'Sports Equipment', 89.99, 8, 7, 6, true, now() - interval '2 days'),
    ('Adjustable Dumbbell Set', 'Weightlifting Equipment', 199.99, 10, 7, 6, true, now() - interval '1 day');

INSERT INTO user_role(name, description)
VALUES ('ADMIN', 'Full authorities'),
       ('USER', 'Basic authorities');

INSERT INTO users(first_name, last_name, email, password,  role_id)
VALUES ('Janusz', 'Kowalski', 'janusz.kowalski@test.pl','{noop}password',  1),
       ('Sebastian', 'Kowalski', 'sebastian.kowalski@test.pl','{noop}password', 2);