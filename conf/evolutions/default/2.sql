-- !Ups

alter table "user"
    add column is_email_verified boolean not null default false;

create table user_invite
(
    id          serial                     not null,
    user_id     int references "user" (id) not null,
    token       varchar(32)                not null,
    is_active   boolean                    not null default false,
    invite_type varchar(6)                 not null default 'NEW',
    creation_ts timestamp                  not null default now()
);

create unique index user_invite_id_uindex
    on user_invite (id);

alter table user_invite
    add constraint user_invite_pk
        primary key (id);

-- !Downs

drop table user_invite;

alter table "user"
    drop column is_email_verified;