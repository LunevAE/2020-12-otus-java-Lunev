create table client
(
    id       bigserial not null primary key,
    login    varchar(255),
    name     varchar(255),
    password varchar(255)
);

create table address_data_set
(
    id       bigserial not null primary key,
    street   varchar(255),
    client_id int8 not null references client(id)
);

create table phone_data_set
(
    id        bigserial not null primary key,
    number    varchar(255),
    client_id int8      not null references client(id)
);