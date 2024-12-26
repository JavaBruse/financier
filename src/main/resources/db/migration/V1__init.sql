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
    created        bigint not null
);

INSERT INTO users (name, password)
VALUES ('bob', '123'),
       ('john', '123');

INSERT INTO categories (user_id, name, max_limit)
VALUES (1, 'Groceries', 1000.00),
       (1, 'Transport', 500.00),
       (1, 'Entertainment', 2000.00),
       (1, 'Bills', 1500.00),
       (2, 'Healthcare', 1200.00),
       (2, 'Transport', 500.00),
       (2, 'Entertainment', 2000.00),
       (2, 'Bills', 1500.00),
       (2, 'Healthcare', 1200.00);

INSERT INTO transactions (user_id, category_id, description, type, money,  created)
VALUES
    (1, 1, 'Weekly groceries shopping', 'OUT', 500.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (2, 8, 'Paycheck', 'IN', 100.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (1, 2, 'Bus ticket', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (2, 5, 'Monthly transport pass', 'OUT', 300.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (1, 3, 'Movie night', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (1, 3, 'Concert tickets', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (2, 6, 'Electricity bill', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (1, 4, 'Water bill', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (1, 5, 'Doctor appointment', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
    (2, 7, 'Monthly healthcare subscription', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
      (1, 1, 'Weekly groceries shopping', 'OUT', 500.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (2, 8, 'Paycheck', 'IN', 100.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (1, 2, 'Bus ticket', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (2, 5, 'Monthly transport pass', 'OUT', 300.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (1, 3, 'Movie night', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (1, 3, 'Concert tickets', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (2, 6, 'Electricity bill', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (1, 4, 'Water bill', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (1, 5, 'Doctor appointment', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
        (2, 7, 'Monthly healthcare subscription', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
          (1, 1, 'Weekly groceries shopping', 'OUT', 500.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (2, 8, 'Paycheck', 'IN', 100.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (1, 2, 'Bus ticket', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (2, 5, 'Monthly transport pass', 'OUT', 300.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (1, 3, 'Movie night', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (1, 3, 'Concert tickets', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (2, 6, 'Electricity bill', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (1, 4, 'Water bill', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (1, 5, 'Doctor appointment', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)),
            (2, 7, 'Monthly healthcare subscription', 'OUT', 200.00, EXTRACT(EPOCH FROM CURRENT_TIMESTAMP));