drop table if exists testbase.users cascade ;
drop table if exists testbase.clients cascade ;
drop table if exists testbase.owners cascade ;
drop table if exists testbase.artists cascade ;
drop table if exists testbase.tickets cascade ;
drop table if exists testbase.reservations cascade ;
drop table if exists testbase.expos cascade ;
drop table if exists testbase.artworks cascade ;
drop table if exists testbase.client_owner_payment cascade ;
drop table if exists testbase.owner_artist_payment cascade ;
drop table if exists testbase.payment cascade ;

create table if not exists testbase.users
(
    id    bigserial primary key ,
    login varchar(255) not null unique ,
    password varchar(255) not null unique ,
    name  varchar(255) not null ,
    email varchar(255) not null unique ,
    authentication bool not null
);

create table if not exists testbase.clients
(
    id    bigserial primary key references testbase.users(id),
    tickets bigint[],
    reservations bigint[]
);

create table if not exists testbase.owners
(
    id    bigserial primary key references testbase.users(id),
    expos varchar[]
);

create table if not exists testbase.artists
(
    id    bigserial primary key references testbase.users(id),
    artworks varchar[]
);

create table if not exists testbase.expos
(
    id    bigserial primary key ,
    name varchar(255) not null unique ,
    info varchar(255) not null ,
    artist  bigint not null references testbase.users(id) ,
    startTime timestamp not null ,
    endTime timestamp not null ,
    ticketPrice double precision not null ,
    artworks varchar[] not null ,
    status varchar(255) not null
);

create table if not exists testbase.tickets
(
    id    bigserial primary key ,
    client bigint not null references testbase.users(id) ,
    expo bigint not null references testbase.expos(id),
    cost  double precision not null
);

create table if not exists testbase.reservations
(
    id    bigserial primary key ,
    client bigint not null references testbase.users(id) ,
    cost  double precision not null ,
    status varchar(255) not null ,
    dateTime timestamp not null,
    tickets bigint[] not null
);

create table if not exists testbase.artworks
(
    id    bigserial primary key ,
    name varchar(255) not null unique,
    info varchar(255) not null ,
    artist  bigint not null references testbase.users(id)
);

create table if not exists testbase.payment
(
    id    bigserial primary key ,
    dateTime timestamp not null ,
    price double precision not null
);

create table if not exists testbase.client_owner_payment
(
    id    bigserial primary key references testbase.payment(id),
    reservation  bigint not null references testbase.reservations(id),
    client bigint not null references testbase.users(id),
    owner bigint not null references testbase.users(id)
);

create table if not exists testbase.owner_artist_payment
(
    id    bigserial primary key references testbase.payment(id),
    expo  bigint not null references testbase.expos(id),
    owner bigint not null references testbase.users(id),
    artist bigint not null references testbase.users(id)
);
