delete from meals;
delete from user_role;
delete from users;
alter SEQUENCE global_seq RESTART with 100000;

insert into users (name, email, password)
values ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

insert into user_role (role, user_id)
values ('USER', 100000),
       ('ADMIN', 100001);

insert into meals (date_time, description, calories, user_id)
values ('2020-01-30 00:00:00', 'Завтрак админа 30', 500, 100001),
       ('2020-01-30 13:00:00', 'Обед админа 30', 1000, 100001),
       ('2020-01-30 23:59:59', 'Ужин админа 30', 500, 100001),
       ('2020-01-31 00:00:00', 'Завтрак админа 31', 100, 100001),
       ('2020-01-31 10:00:00', 'Обед админа 31', 1000, 100001),
       ('2020-01-31 23:59:59', 'Ужин админа 31', 901, 100001),
       ('2022-01-31 00:00:00', 'Обед юзера', 500, 100000),
       ('2022-01-31 23:59:00', 'Ужин юзера', 410, 100000);
