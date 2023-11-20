
truncate table bookings, categories, cinema_halls, coupons, customers, hall_seats, movie_categories, movie_showings, movies, tickets restart identity cascade;

drop table if exists first_names cascade;

create table  first_names (
    first_name varchar(50)
);

insert into first_names(first_name) values
                                        ('Peter'), ('Anton'), ('Ján'), ('Emil'), ('Matúš'),
                                        ('Bruno'), ('Gabriel'), ('Daniel'), ('Andrej'), ('Marek'),
                                        ('Erik'), ('Vlado'), ('Igor'), ('Milan'), ('Norbert'),
                                        ('Ivan'), ('Jozef'), ('Jakub'), ('Henrich'), ('Martin'),
                                        ('Vít'), ('Hugo'), ('Alfred'), ('Rudolf'), ('Filip'),
                                        ('Ctibor'), ('Dezider'), ('Marián'), ('Branislav'), ('Karol');

drop table if exists last_names cascade;

create table  last_names (
    last_name varchar(50)
);

insert into last_names(last_name) values
                                        ('Vach'), ('Mihok'), ('Laurinec'), ('Klányi'), ('Vasky'),
                                        ('Daňo'), ('Blaha'), ('Molnár'), ('Jurečka'), ('Novák'),
                                        ('Granec'), ('Maštalír'), ('Tóth'), ('Ursíny'), ('Dočolomanský'),
                                        ('Kotleba'), ('Danko'), ('Sulík'), ('Fico'), ('Harabin'),
                                        ('Matovič'), ('Slota'), ('Mečiar'), ('Kiska'), ('Fischer'),
                                        ('Drevo'), ('Kuťka'), ('Jakubec'), ('Kandráč'), ('Kollár');

drop table if exists months cascade;
create table  months (
    month varchar(50)
);

insert into months(month) values
                                      ('1'), ('3'), ('4'), ('5'),
                                      ('6'), ('7'), ('8'), ('9'), ('10'),
                                      ('11'), ('12');   /*TODO februar doriesit*/

create or replace function random_first_name() returns varchar language sql as
$$
select first_name from first_names order by random()+1 limit 1;
$$;

create or replace function random_last_name() returns varchar language sql as
$$
select last_name from last_names order by random()+1 limit 1;
$$;

create or replace function random_month() returns varchar language sql as
$$
select month from months order by random()+1 limit 1;
$$;

do $$
    declare fName varchar;
    declare lName varchar;
    declare ddate varchar;
    declare yyear int;
    declare age int;
    begin
        for r in 1..100
            loop
                fName := random_first_name();
                lName := random_last_name();
                yyear := floor(random() * (2004-1950)+1950);
                age := 2022 - yyear;
                ddate := yyear || '-' || random_month() || '-' || floor(random()*30+1);
                insert into customers (customer_first_name, customer_last_name, customer_mail, customer_age, customer_date_of_birth)
                select fName,
                       lName,
                       fName || '.' || lName || '@gmail.com',
                       age,
                       cast(ddate as date);
            end loop;
end $$;

insert into cinema_halls (cinema_hall_capacity, cinema_hall_row_count) values
                                                                                (50,5), (100,10);

do $$
    declare num int;
    begin
        for r in 0..49
            loop
            num = r;
            insert into hall_seats (cinema_hall_id, hall_seat_number, hall_seat_row)
            select
                c.cinema_hall_id,
                num + 1 as hall_seat_number,
                (num / 10 + 1) as hall_seat_row
            from cinema_halls as c where cinema_hall_capacity = 50;
            end loop;
end $$;

do $$
    declare num int;
    begin
        for r in 0..99
            loop
                num = r;
                insert into hall_seats (cinema_hall_id, hall_seat_number, hall_seat_row)
                select
                    c.cinema_hall_id,
                    num + 1 as hall_seat_number,
                    (num / 10 + 1) as hall_seat_row
                from cinema_halls as c where cinema_hall_capacity = 100;
            end loop;
    end $$;

insert into categories (category_name) values
                                             ('Action'), ('Adventure'), ('Animation'), ('Comedy'),
                                             ('Crime'), ('Drama'), ('Family'), ('Fantasy'),
                                             ('Film-Noir'), ('History'), ('Horror'), ('Mystery'),
                                             ('Romance'), ('Sci-Fi'), ('Thriller'), ('War'),
                                             ('Western');

insert into movies (movie_name, movie_minutes, movie_avg_rating, movie_description, movie_year)
values
       ('The Shawshank Redemption', 142, 92, 'Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.', cast('1994-1-1' as date)),
       ('Inception', 148, 89, 'A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.', cast('2010-1-1' as date)),
       ('The Lord of the Rings: The Return of the King', 201, 88, 'Gandalf and Aragorn lead the World of Men against Sauron''s army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.', cast('2003-1-1' as date)),
       ('Pulp Fiction', 141, 89, 'The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.', cast('1994-1-1' as date)),
       ('Tenet', 150, 74, 'Armed with only one word, Tenet, and fighting for the survival of the entire world, a Protagonist journeys through a twilight world of international espionage on a mission that will unfold in something beyond real time.', cast('2020-1-1' as date)),
       ('L.A. Confidential', 138, 83, 'As corruption grows in 1950s Los Angeles, three policemen - one strait-laced, one brutal, and one sleazy - investigate a series of murders with their own brand of justice.',cast('1997-1-1' as date)),
       ('Mononoke-hime', 134, 84, 'On a journey to find the cure for a Tatarigami''s curse, Ashitaka finds himself in the middle of a war between the forest gods and Tatara, a mining colony. In this quest he also meets San, the Mononoke Hime.',cast('1997-1-1' as date)),
       ('A Clockwork Orange', 136, 83, 'In the future, a sadistic gang leader is imprisoned and volunteers for a conduct-aversion experiment, but it doesn''t go as planned.', cast('1971-1-1' as date));

insert into movie_categories (category_id, movie_id)
VALUES
       (5,1), (6,1),
       (1,2), (12,2), (14,2),
       (2,3), (8,3),
       (1,4), (5,4),
       (1,5), (12,5), (14,5),
       (5,6), (15,6),
       (2,7), (3,7), (8,7),
       (6,8), (14,8);

create or replace function random_movie() returns int language sql as
$$
select movie_id from movies order by random()*8 limit 1;
$$;

do $$
    declare time varchar;
    declare ddate varchar;
    begin
        for r in 1..1000
            loop
                time := timestamp '2020-01-10 14:00:00' +
                        random() * (timestamp '2022-05-01 20:00:00' -
                                    timestamp '2020-01-01 10:00:00');

                insert into movie_showings (cinema_hall_id, movie_id, movie_showing_time_from, movie_showing_date)
                select (floor(random()*2+1)),
                       random_movie(),
                       cast(time as time),
                       cast(time as date);
            end loop;
    end $$;

create or replace function random_customer() returns int language sql as
$$
select customer_id from customers order by random()+1 limit 1;
$$;


drop table if exists coupon_texts cascade;

create table  coupon_texts (
    coupon_text varchar(50)
);
insert into coupon_texts(coupon_text) values
                                      ('KINO20'), ('FILMY10'), ('ZLAVA15');

create or replace function random_text() returns varchar language sql as
$$
select coupon_text from coupon_texts order by random()+1 limit 1;
$$;

do $$
    declare price integer;
    declare ddate varchar;
    declare text varchar;
    begin
        for r in 1..20
            loop
                price := floor(random()*(10-5)+5);
                ddate := 2022 || '-' || random_month() || '-' || floor(random()*30+1);
                text := random_text();
                insert into coupons (customer_id, coupon_text, coupon_valid_until, coupon_price)
                select random_customer(),
                       text,
                       cast(ddate as date),
                       price;
            end loop;
    end $$;

do $$
    declare customer int;
    declare coupon int;
    begin
        for r in 1..50
            loop
                customer := random_customer();
                IF exists(select 1 from coupons where customer_id = customer) then
                    coupon := coupon_id from coupons where customer_id = customer limit 1;
                else
                    coupon := null;
                end if;

                insert into bookings (customer_id, coupon_id, booking_price, booking_created_date, booking_created_time)
                select customer as customer_id,
                       coupon,
                       0,
                       current_date,
                       current_time;

            end loop;
    end $$;

create or replace function random_booking() returns int language sql as
$$
select booking_id from bookings order by random()+1 limit 1;
$$;

create or replace function free_seat(hall int) returns int language sql as
$$
select hall_seat_id from hall_seats where hall_seat_sold = false AND cinema_hall_id = hall order by random()+1 limit 1;
$$;

create or replace function random_showing() returns int language sql as
$$
select movie_showing_id from movie_showings order by random() limit 1;
$$;

drop table if exists types cascade;
create table  types (
    type varchar(50)
);

insert into types(type) values
                              ('discount'), ('basic');

create or replace function random_type() returns varchar language sql as
$$
select type from "types" order by random()+1 limit 1;
$$;

create or replace function random_booking() returns int language sql as
$$
select booking_id from bookings order by random()+1 limit 1;
$$;

do $$
    declare ticket_price integer;
    declare bbooking integer;
    declare ttype varchar;
    declare sshowing integer;
    declare movie integer;
    declare hhall integer;
    declare sseat integer;
    begin
        for r in 1..100
            loop
                ttype := random_type();
                if  ttype = 'basic' then
                    ticket_price := 8;
                else
                    ticket_price := 5;
                end if;
                bbooking := random_booking();
                sshowing := random_showing();
                movie := movie_id from movie_showings where movie_showing_id = sshowing;
                hhall := cinema_hall_id from movie_showings where movie_showing_id = sshowing;
                sseat := free_seat(hhall);

                update hall_seats set hall_seat_sold = true where hall_seat_id = sseat and cinema_hall_id = hhall;

                insert into tickets (booking_id, hall_seat_id, movie_id, movie_showing_id, ticket_price, ticket_type)
                select bbooking,
                       sseat,
                       movie,
                       sshowing,
                       ticket_price,
                       ttype;
            end loop;
    end $$;

do $$
        declare bbooking integer;
        declare ttype varchar;
        declare total integer;
        declare r record;

    begin
        for r in SELECT * FROM bookings
            loop
                ttype := random_type();
                bbooking := random_booking();
                total := SUM(ticket_price) FROM tickets WHERE booking_id = r.booking_id;
                IF total is null then
                    total := 0;
                end if;

                update bookings SET booking_price = total WHERE booking_id = r.booking_id;

            end loop;
    end $$;


drop table if exists first_names, last_names, months, types, coupon_texts cascade;
drop function random_first_name();
drop function random_last_name();
drop function random_month();
drop function random_customer();
drop function random_movie();
drop function random_type();
drop function random_showing();
drop function free_seat(hall int);
drop function random_booking();
drop function random_text();