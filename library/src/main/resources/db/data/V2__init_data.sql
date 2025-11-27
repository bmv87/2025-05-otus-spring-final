 insert into users(username, first_name, last_name, middle_name, birthday)
values ('admin', 'Mary',  'B', 'V', '2012-08-31'),
       ('reader1', 'Tony',  'T', 'S', '1977-08-31'),
       ('reader2', 'Bill',  'F', 'C', '2002-08-31');

insert into roles(name, description)
values ('ADMIN', 'Main Administrator'), ('READER', 'Book reader');

insert into users_roles(user_id, role_id)
values (1, 1), (2, 2), (3, 2);

insert into sections(name, created_by)
values ('Научная литература', 1), ('Информационные технологии', 1),
 	('Классика', 1), ('Психология', 1), ('Образование', 1);