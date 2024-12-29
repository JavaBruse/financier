CREATE TABLE users (
    id         SERIAL PRIMARY KEY,
    name       varchar(36) not null,
    password   varchar(80) not null
);

CREATE TABLE categories (
    id             SERIAL PRIMARY KEY,
    user_id        bigint not null references users (id),
    name           varchar(36) not null,
    max_limit      decimal not null
);

CREATE TABLE transactions (
    id             SERIAL PRIMARY KEY,
    user_id        bigint not null references users (id),
    category_id    bigint not null references categories (id),
    description    varchar(150) not null,
    money          decimal not null,
    type           varchar(36) not null,
    created        bigint not null DEFAULT (EXTRACT(EPOCH FROM NOW()) * 1000)::bigint
);

INSERT INTO users (name, password)
VALUES ('bob', '123'),
       ('john', '123');

INSERT INTO categories (user_id, name, max_limit)
VALUES (1, 'Еда', 4000.00),
       (1, 'Развлечения', 3000.00),
       (1, 'Коммунальные услуги', 2500.00),
       (1, 'Прочие расходы', 10000.00),
       (1, 'Зарплата', 0.00);



INSERT INTO transactions (user_id, category_id, description, type, money)
VALUES
    (1, 1, 'Купил еды', 'OUT', -300.00),
    (1, 1, 'Купил еды', 'OUT', -500.00),
    (1, 2, 'Погулял в клубе', 'OUT', -3000.00),
    (1, 3, 'Заплатил коммуналку', 'OUT', -3000.00),
    (1, 4, 'За такси', 'OUT', -1500.00),
    (1, 5, 'зарплата за январь', 'IN', 20000.00),
    (1, 5, 'Зарплата за февраль', 'IN', 40000.00),
    (1, 5, 'Премия', 'IN', 3000.00);
