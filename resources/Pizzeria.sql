CREATE DATABASE pizzeria2;

CREATE TABLE pizza
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(64) UNIQUE NOT NULL,
    image bytea
);

CREATE TABLE ingredient
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(64) UNIQUE NOT NULL,
    price NUMERIC(3, 2)      NOT NULL
);

CREATE TABLE pizza_ingredient
(
    id            SERIAL PRIMARY KEY,
    pizza_id      INT REFERENCES pizza (id) ON DELETE CASCADE        NOT NULL,
    ingredient_id INT REFERENCES ingredient (id) ON DELETE NO ACTION NOT NULL,
    UNIQUE (pizza_id, ingredient_id)
);

CREATE TABLE users
(
    id           BIGSERIAL PRIMARY KEY,
    firstname    VARCHAR(64)        NOT NULL,
    lastname     VARCHAR(64)        NOT NULL,
    phone_number VARCHAR(64) UNIQUE NOT NULL,
    email        VARCHAR(64) UNIQUE NOT NULL,
    role         VARCHAR(32)        NOT NULL,
    birth_date   DATE,
    password     VARCHAR(64)        NOT NULL
);

CREATE TABLE delivery_address
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    address VARCHAR(128)                                   NOT NULL
);

CREATE TABLE pizza_to_order
(
    id       BIGSERIAL PRIMARY KEY,
    pizza_id INT REFERENCES pizza (id) ON DELETE NO ACTION    NOT NULL,
    size     VARCHAR(32)                                      NOT NULL,
    type     VARCHAR(32)                                      NOT NULL,
    count    INT                                              NOT NULL,
    price    NUMERIC(5, 2)                                    NOT NULL,
    user_id  BIGINT REFERENCES users (id) ON DELETE NO ACTION NOT NULL
);

CREATE TABLE ingredient_to_order
(
    id                BIGSERIAL PRIMARY KEY,
    pizza_to_order_id BIGINT REFERENCES pizza_to_order (id) ON DELETE NO ACTION NOT NULL,
    ingredient_id     INT REFERENCES ingredient (id) ON DELETE NO ACTION        NOT NULL
);

CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY,
    date_time   TIMESTAMP     NOT NULL,
    final_price NUMERIC(5, 2) NOT NULL
);

CREATE TABLE order_detail
(
    id                BIGSERIAL PRIMARY KEY,
    order_id          BIGINT REFERENCES orders (id) ON DELETE NO ACTION       NOT NULL,
    pizza_to_order_id BIGINT REFERENCES pizza_to_order (id) ON DELETE CASCADE NOT NULL,
    price             NUMERIC(5, 2)                                           NOT NULL,
    UNIQUE (order_id, pizza_to_order_id)
);