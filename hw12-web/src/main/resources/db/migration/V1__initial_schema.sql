create sequence hibernate_sequence;

create table address_data_set (
    id       bigint not null,
    street   varchar(255),
    client_id bigint,
    primary key (id));

create table client (
    id       bigint not null,
    login    varchar(255),
    name     varchar(255),
    password varchar(255),
    primary key (id));

create table phone_data_set (
    id        bigint not null,
    number    varchar(255),
    client_id bigint not null,
    primary key (id));

insert into client (id, login, name, password) values (1, 'client1', 'client1', '111');
insert into phone_data_set (client_id, number, id) values (1, '+79994448811', 1);
insert into phone_data_set (client_id, number, id) values (1, '+79983322554', 2);
insert into address_data_set (client_id, street, id) values (1, 'street1', 1);

SELECT nextval('hibernate_sequence');