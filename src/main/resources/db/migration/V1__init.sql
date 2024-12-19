create table users (
                       id         bigint primary key,
                       name       varchar(36) not null,
                       password   varchar(80) not null,
);



CREATE TABLE categories (
                             id             bigint primary key,
                             user_id        bigint not null references users (id),
                             name           varchar(36) not null,
                             limit          double not null,
                             primary key (user_id)
);

CREATE TABLE transactions (
                             id             bigint primary key,
                             user_id        bigint not null references users (id),
                             categories_id  bigint not null references categories (id),
                             description    varchar(150) not null,
                             type           varchar(36) not null,
                             created        double not null,
                             primary key (user_id, categories_id)
);





insert into users (name, password)
values ('bob', '123'),
       ('john', '123');

