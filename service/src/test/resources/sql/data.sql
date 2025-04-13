INSERT INTO users (firstname, lastname, phone_number, email, role, birth_date, password)
VALUES ('Ivan', 'Ivanov', '11-11-111', 'ivan@gmail.com', 'ADMIN', '1995-05-15', '111'),
       ('Petr', 'Petrov', '22-22-222', 'petr@gmail.com', 'USER', '1999-07-04', '222'),
       ('Dima', 'Dimov', '33-33-333', 'dima@gmail.com', 'USER', '1990-11-28', '333');

INSERT INTO delivery_address (user_id, address)
VALUES ((SELECT id FROM users WHERE phone_number = '11-11-111'), 'Moscow'),
       ((SELECT id FROM users WHERE phone_number = '11-11-111'), 'Kaluga'),
       ((SELECT id FROM users WHERE phone_number = '22-22-222'), 'Brest'),
       ((SELECT id FROM users WHERE phone_number = '33-33-333'), 'Minsk');

INSERT INTO pizza (name)
VALUES ('Pepperoni'),
       ('Italian'),
       ('Four cheeses');

INSERT INTO ingredient (name, price)
VALUES ('Mozzarella', 3.5),
       ('Tomato sauce', 2.5),
       ('Bacon', 3.0),
       ('Parmesan', 3.5),
       ('Onion', 2.0);

INSERT INTO pizza_ingredient (pizza_id, ingredient_id)
VALUES ((SELECT id FROM pizza WHERE name = 'Pepperoni'), (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza WHERE name = 'Pepperoni'), (SELECT id FROM ingredient WHERE name = 'Tomato sauce')),
       ((SELECT id FROM pizza WHERE name = 'Italian'), (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza WHERE name = 'Italian'), (SELECT id FROM ingredient WHERE name = 'Bacon')),
       ((SELECT id FROM pizza WHERE name = 'Italian'), (SELECT id FROM ingredient WHERE name = 'Onion')),
       ((SELECT id FROM pizza WHERE name = 'Four cheeses'), (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza WHERE name = 'Four cheeses'), (SELECT id FROM ingredient WHERE name = 'Parmesan')),
       ((SELECT id FROM pizza WHERE name = 'Four cheeses'), (SELECT id FROM ingredient WHERE name = 'Onion'));

INSERT INTO pizza_to_order (pizza_id, size, type, count, price, user_id)
VALUES ((SELECT id FROM pizza WHERE name = 'Pepperoni'), 'BIG', 'THIN', 1, 35,
        (SELECT id FROM users WHERE email = 'petr@gmail.com')),
       ((SELECT id FROM pizza WHERE name = 'Italian'), 'SMALL', 'THIN', 2, 25,
        (SELECT id FROM users WHERE email = 'petr@gmail.com')),
       ((SELECT id FROM pizza WHERE name = 'Pepperoni'), 'MEDIUM', 'TRADITIONAL', 2, 30,
        (SELECT id FROM users WHERE email = 'dima@gmail.com')),
       ((SELECT id FROM pizza WHERE name = 'Four cheeses'), 'BIG', 'THIN', 2, 35,
        (SELECT id FROM users WHERE email = 'dima@gmail.com')),
       ((SELECT id FROM pizza WHERE name = 'Italian'), 'SMALL', 'TRADITIONAL', 1, 25,
        (SELECT id FROM users WHERE email = 'dima@gmail.com')),
       ((SELECT id FROM pizza WHERE name = 'Four cheeses'), 'MEDIUM', 'TRADITIONAL', 3, 30,
        (SELECT id FROM users WHERE email = 'ivan@gmail.com'));

INSERT INTO ingredient_to_order (pizza_to_order_id, ingredient_id)
VALUES ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Pepperoni') AND
    size = 'BIG' AND
    type = 'THIN' AND
    count = 1 AND
    price = 35 AND
    user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')),
       (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Pepperoni') AND
           size = 'BIG' AND
           type = 'THIN' AND
           count = 1 AND
           price = 35 AND
           user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Tomato sauce')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
           size = 'SMALL' AND
           type = 'THIN' AND
           count = 2 AND
           price = 25 AND
           user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
           size = 'SMALL' AND
           type = 'THIN' AND
           count = 2 AND
           price = 25 AND
           user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Bacon')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
           size = 'SMALL' AND
           type = 'THIN' AND
           count = 2 AND
           price = 25 AND
           user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Onion')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Pepperoni') AND
           size = 'MEDIUM' AND
           type = 'TRADITIONAL' AND
           count = 2 AND
           price = 30 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Pepperoni') AND
           size = 'MEDIUM' AND
           type = 'TRADITIONAL' AND
           count = 2 AND
           price = 30 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Tomato sauce')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
           size = 'BIG' AND
           type = 'THIN' AND
           count = 2 AND
           price = 35 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
           size = 'BIG' AND
           type = 'THIN' AND
           count = 2 AND
           price = 35 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Parmesan')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
           size = 'BIG' AND
           type = 'THIN' AND
           count = 2 AND
           price = 35 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Onion')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
           size = 'SMALL' AND
           type = 'TRADITIONAL' AND
           count = 1 AND
           price = 25 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
           size = 'SMALL' AND
           type = 'TRADITIONAL' AND
           count = 1 AND
           price = 25 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Bacon')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
           size = 'SMALL' AND
           type = 'TRADITIONAL' AND
           count = 1 AND
           price = 25 AND
           user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Onion')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
           size = 'MEDIUM' AND
           type = 'TRADITIONAL' AND
           count = 3 AND
           price = 30 AND
           user_id = (SELECT id FROM users WHERE email = 'ivan@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Mozzarella')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
           size = 'MEDIUM' AND
           type = 'TRADITIONAL' AND
           count = 3 AND
           price = 30 AND
           user_id = (SELECT id FROM users WHERE email = 'ivan@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Parmesan')),
       ((SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
           size = 'MEDIUM' AND
           type = 'TRADITIONAL' AND
           count = 3 AND
           price = 30 AND
           user_id = (SELECT id FROM users WHERE email = 'ivan@gmail.com')),
        (SELECT id FROM ingredient WHERE name = 'Onion'));

INSERT INTO orders (date_time, final_price)
VALUES ('2025-03-18 14:30:00', 85),
       ('2025-03-23 16:35:00', 60),
       ('2025-03-25 18:30:00', 90),
       ('2025-03-28 11:30:00', 95);

INSERT INTO order_detail (order_id, pizza_to_order_id, price)
VALUES ((SELECT id FROM orders WHERE date_time = '2025-03-18 14:30:00' AND final_price = 85),
        (SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Pepperoni') AND
            size = 'BIG' AND
            type = 'THIN' AND
            count = 1 AND
            price = 35 AND
            user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')), 35),

       ((SELECT id FROM orders WHERE date_time = '2025-03-18 14:30:00' AND final_price = 85),
        (SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
            size = 'SMALL' AND
            type = 'THIN' AND
            count = 2 AND
            price = 25 AND
            user_id = (SELECT id FROM users WHERE email = 'petr@gmail.com')), 50),

       ((SELECT id FROM orders WHERE date_time = '2025-03-23 16:35:00' AND final_price = 60),
        (SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Pepperoni') AND
            size = 'MEDIUM' AND
            type = 'TRADITIONAL' AND
            count = 2 AND
            price = 30 AND
            user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')), 60),

       ((SELECT id FROM orders WHERE date_time = '2025-03-25 18:30:00' AND final_price = 90),
        (SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
            size = 'MEDIUM' AND
            type = 'TRADITIONAL' AND
            count = 3 AND
            price = 30 AND
            user_id = (SELECT id FROM users WHERE email = 'ivan@gmail.com')), 90),

       ((SELECT id FROM orders WHERE date_time = '2025-03-28 11:30:00' AND final_price = 95),
        (SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Four cheeses') AND
            size = 'BIG' AND
            type = 'THIN' AND
            count = 2 AND
            price = 35 AND
            user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')), 70),

       ((SELECT id FROM orders WHERE date_time = '2025-03-28 11:30:00' AND final_price = 95),
        (SELECT id FROM pizza_to_order WHERE pizza_id = (SELECT id FROM pizza WHERE name = 'Italian') AND
            size = 'SMALL' AND
            type = 'TRADITIONAL' AND
            count = 1 AND
            price = 25 AND
            user_id = (SELECT id FROM users WHERE email = 'dima@gmail.com')), 25);