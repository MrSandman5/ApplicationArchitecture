drop table if exists testbase.users cascade ;
drop table if exists testbase.tickets cascade ;
drop table if exists testbase.reservations cascade ;
drop table if exists testbase.expos cascade ;
drop table if exists testbase.artworks cascade ;
drop table if exists testbase.client_owner_payment cascade ;
drop table if exists testbase.owner_artist_payment cascade ;

create table if not exists users
(
    id    bigserial primary key ,
    login varchar(255) not null ,
    password varchar(255) not null ,
    name  varchar(255) not null ,
    email varchar(255) not null ,
    authentication bool not null ,
    role varchar(255) not null
);

create table if not exists expos
(
    id    bigserial primary key ,
    name varchar(255) not null ,
    info varchar(255) not null ,
    artist  bigint not null references testbase.users(id) ,
    startTime timestamp not null ,
    endTime timestamp not null ,
    status varchar(255) not null
);

create table if not exists tickets
(
    id    bigserial primary key ,
    client bigint not null references testbase.users(id) ,
    expo bigint not null references testbase.expos(id),
    cost  double precision not null
);

create table if not exists reservations
(
    id    bigserial primary key ,
    client bigint not null references testbase.users(id) ,
    cost  double precision not null ,
    status varchar(255) not null ,
    dateTime timestamp not null
);

create table if not exists artworks
(
    id    bigserial primary key ,
    name varchar(255) not null ,
    info varchar(255) not null ,
    artist  bigint not null references testbase.users(id)
);

create table if not exists client_owner_payment
(
    id    bigserial primary key ,
    date timestamp not null ,
    amount double precision not null ,
    reservation  bigint not null references testbase.reservations(id),
    client bigint not null references testbase.users(id),
    owner bigint not null references testbase.users(id)
);

create table if not exists owner_artist_payment
(
    id    bigserial primary key ,
    date timestamp not null ,
    amount double precision not null ,
    expo  bigint not null references testbase.expos(id),
    owner bigint not null references testbase.users(id),
    artist bigint not null references testbase.users(id)
);
