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
values ('2020-01-30 10:00:00', 'Завтрак админа', 500, 100001),
       ('2020-01-30 13:00:00', 'Обед админа', 1000, 100001),
       ('2020-01-30 20:00:00', 'Ужин админа', 500, 100001),
       ('2020-01-31 00:00:00', 'Еда на граничное значение админа', 100, 100001),
       ('2020-01-31 10:00:00', 'Завтрак админа', 1000, 100001),
       ('2020-01-31 13:00:00', 'Обед админа', 500, 100001),
       ('2020-01-31 20:00:00', 'Ужин админа', 410, 100001),
       ('2022-01-31 13:00:00', 'Обед юзера', 500, 100000),
       ('2022-01-31 20:00:00', 'Ужин юзера', 410, 100000);
