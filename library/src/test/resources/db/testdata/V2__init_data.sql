insert into users(username, first_name, last_name, middle_name, birthday)
values ('admin', 'Mary',  'B', 'V', '2012-08-31'),
       ('reader1', 'Tony',  'T', 'S', '1977-08-31'),
       ('reader2', 'Bill',  'F', 'C', '2002-08-31');

insert into roles(name, description)
values ('ADMIN', 'Main Administrator'), ('READER', 'Book reader');

insert into users_roles(user_id, role_id)
values (1, 1), (2, 2), (3, 2);

insert into sections(name, created_by)
values ('section_1', 1), ('section_2', 1), ('section_3', 1);

insert into authors(full_name,description,created_by,created_at) values
	 ('author_1','description_1',1,'2025-11-16 20:34:30.658039'),
	 ('author_2','description_2',1,'2025-11-16 20:34:30.658039'),
	 ('author_3','description_3',1,'2025-11-16 20:34:30.658039');

insert into books(title,description,publication_year,created_by,created_at) values
	 ('book_1','description_1',2023,1,'2025-11-19 21:13:37.77455'),
	 ('book_2','description_2',2024,1,'2025-11-19 21:13:37.77455'),
	 ('book_3','description_3',2025,1,'2025-11-19 21:13:37.77455');

insert into books_authors(book_id,author_id,created_at) values
	 (1,1,'2025-11-21 23:31:07.032808'),
	 (1,2,'2025-11-21 23:31:07.032808'),
	 (2,2,'2025-11-21 23:31:07.032808'),
	 (3,3,'2025-11-21 23:31:07.032808');

insert into books_sections(book_id,section_id,created_at) values
	 (1,1,'2025-11-21 23:31:07.032808'),
	 (1,2,'2025-11-21 23:31:07.032808'),
	 (2,2,'2025-11-21 23:31:07.032808'),
	 (3,3,'2025-11-21 23:31:07.032808');

insert into links(book_id,type,title,target,created_by,created_at) values
	 (1,'application/pdf','book_1.pdf','link_1_1.pdf',1,'2025-11-20 01:19:27.467993'),
	 (1,'application/epub+zip','book_1.epub','link_1_2.epub',1,'2025-11-20 01:23:42.549614'),
	 (1,'application/vnd.djvu','book_1.djvu','link_1_3.djvu',1,'2025-11-20 01:32:49.157775');

insert into pictures(book_id,type,title,target,created_by,created_at) values
	 (1,'image/webp','book_pic_1.webp','picture_1_1.webp',1,'2025-11-19 23:15:57.97864'),
	 (1,'image/webp','book_pic_2.webp','picture_1_2.webp',1,'2025-11-20 00:52:02.015986');

