/*Filmy */
drop table if exists movies cascade;
create table if not exists movies
(
    movie_id          serial
        constraint movies_pk
            primary key,
    movie_name        varchar(100),
    movie_minutes     integer,
    movie_avg_rating  integer,
    movie_description text,
    movie_year        date
);

comment on table movies is 'filmy v kine';

/*Zakaznici */

drop table if exists customers cascade;
create table if not exists customers
(
    customer_id          serial
        constraint customers_pk
            primary key ,
    customer_first_name    varchar(50) not null,
    customer_last_name     varchar(50) not null,
    customer_mail          varchar(50) not null,
    customer_age           integer not null ,
    customer_date_of_birth date
);

comment on table customers is 'zakaznici';

/*Kategorie */

drop table if exists categories cascade;
create table if not exists categories
(
    category_id serial
        constraint categories_pk
            primary key,
    category_name varchar(50)
);

comment on table categories is 'kategorie';

create unique index if not exists categories_categories_id_uindex
    on categories(category_id);

/*Filmove kategorie */
drop table if exists movie_categories cascade;
create table if not exists movie_categories
(
    category_id integer not null,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE ,
    movie_id    integer not null,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id) ON DELETE CASCADE
);

comment on table movie_categories is 'prepojenie medzi movies a categories';

/*Kinosaly */

drop table if exists cinema_halls cascade;
create table if not exists cinema_halls
(
    cinema_hall_id        serial
        constraint cinema_halls_pk
            primary key,
    cinema_hall_capacity  integer,
    cinema_hall_row_count integer
);

comment on table cinema_halls is 'kinosaly';

create unique index if not exists cinema_halls_cinema_hall_id_uindex
    on cinema_halls (cinema_hall_id);

/*Sedadla */

drop table if exists hall_seats cascade;
create table if not exists hall_seats
(
    hall_seat_id     serial
        constraint hall_seats_pk
            primary key,
    cinema_hall_id   integer,
    hall_seat_number integer,
    hall_seat_sold   boolean default false,
    hall_seat_row    integer
);

comment on table hall_seats is 'sedadla';

create unique index if not exists hall_seats_hall_seat_id_uindex
    on hall_seats (hall_seat_id);

/*Kupony */

drop table if exists coupons cascade;
create table if not exists coupons
(
    coupon_id          serial
        constraint coupons_pk
            primary key,
    customer_id integer ,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE SET NULL ,
    coupon_text varchar,
    coupon_valid_until date,
    coupon_price       integer not null ,
    coupon_used        boolean default false
);

comment on table coupons is 'kupony';

/*Predstavenia */

drop table if exists movie_showings cascade;
create table if not exists movie_showings
(
    movie_showing_id        serial
        constraint movie_showings_pk
            primary key,
    cinema_hall_id          integer not null ,
    FOREIGN KEY (cinema_hall_id) REFERENCES cinema_halls(cinema_hall_id) ,
    movie_id                integer,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    movie_showing_time_from time,
    movie_showing_date      date,
    movie_showings_active   bool default true
);

create unique index if not exists movie_showings_movie_showing_id_uindex
    on movie_showings (movie_id, cinema_hall_id, movie_showing_time_from);

/*Rezervacie */

drop table if exists bookings cascade;
create table if not exists bookings
(
    booking_id           serial
        constraint bookings_pk
            primary key,
    customer_id          integer NOT NULL DEFAULT -1,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE SET DEFAULT ,
    coupon_id            integer default null,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE NO ACTION ,
    booking_price        integer DEFAULT 0,
    booking_created_date date not null,
    booking_created_time time not null,
    booking_reserved     boolean DEFAULT false,
    booking_sold         boolean DEFAULT false
);

comment on table bookings is 'rezervacie';

create unique index if not exists bookings_booking_id_uindex
    on bookings (booking_id);

/*Listky */

drop table if exists tickets cascade;
create table if not exists tickets
(
    ticket_id       serial
        constraint tickets_pk
            primary key,
    booking_id       integer default null,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET DEFAULT ,
    hall_seat_id     integer,
    FOREIGN KEY (hall_seat_id) REFERENCES hall_seats(hall_seat_id),
    movie_id         integer,
    FOREIGN KEY (movie_id) REFERENCES movies(movie_id),
    movie_showing_id integer not null,
    FOREIGN KEY (movie_showing_id) REFERENCES movie_showings(movie_showing_id),
    ticket_price     integer,
    ticket_type      varchar(10)
);

comment on table tickets is 'listky';

create unique index if not exists tickets_ticket_id_uindex
    on tickets (ticket_id);





