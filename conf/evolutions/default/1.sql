-- !Ups

create table user_role
(
    id          serial      not null,
    role        varchar(32) not null,
    creation_ts timestamp   not null default now(),
    updation_ts timestamp
);

create unique index user_role_role_uindex
    on user_role (role);

create unique index user_role_id_uindex
    on user_role (id);

alter table user_role
    add constraint user_role_pk
        primary key (id);

insert into user_role (role)
values ('USER');
insert into user_role (role)
values ('ADMIN');

create table "user"
(
    id             serial                           not null,
    first_name     varchar(50)                      not null,
    last_name      varchar(50)                      not null,
    email          varchar(320)                     not null,
    password_hash  varchar(64)                      not null,
    address_line_1 text,
    address_line_2 text,
    picture_url    text,
    is_active      boolean                          not null default false,
    role_id        bigint references user_role (id) not null default (1),
    creation_ts    timestamp                        not null default now(),
    updation_ts    timestamp
);

create unique index user_email_uindex
    on "user" (email);

create unique index user_id_uindex
    on "user" (id);

alter table "user"
    add constraint user_pk
        primary key (id);

-- !Downs

DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS user_role;
