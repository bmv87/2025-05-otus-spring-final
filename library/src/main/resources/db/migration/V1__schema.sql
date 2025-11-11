create table users (
    id bigserial NOT NULL,
    username varchar(30) NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(150) NOT NULL,
    middle_name varchar(100),
    birthday date NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (id)
);
ALTER TABLE users ADD CONSTRAINT unique_uk_username UNIQUE(username);

create table roles (
    id bigserial NOT NULL,
    name varchar(255) NOT NULL,
    description varchar(255),
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (id)
);
ALTER TABLE roles ADD CONSTRAINT unique_uk_name UNIQUE(name);

create table sections (
    id bigserial NOT NULL,
    name varchar(250) NOT NULL,
    created_by integer references users(id) NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (id)
);

create table links (
    id integer NOT NULL,
    type varchar NOT NULL,
    title varchar(250) NOT NULL,
    target varchar NOT NULL,
    created_by integer references users(id) NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (id)
);

create table authors (
    id bigserial NOT NULL,
    full_name varchar(255) NOT NULL,
    description varchar NOT NULL,
    created_by integer references users(id) NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (id)
);

create table books (
    id bigserial NOT NULL,
    title varchar(255) NOT NULL,
    description varchar(255),
    publication_year integer NOT NULL,
    created_by integer references users(id) NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (id)
);

create table books_authors (
    book_id bigint references books(id) on delete cascade NOT NULL,
    author_id bigint references authors(id) on delete cascade NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (book_id, author_id)
);

create table books_sections (
    book_id bigint references books(id) on delete cascade NOT NULL,
    section_id bigint references sections(id) on delete cascade NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (book_id, section_id)
);

create table users_roles (
    user_id bigint references users(id) on delete cascade NOT NULL,
    role_id bigint references roles(id) on delete cascade NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (user_id, role_id)
);

create table books_links (
    book_id bigint references books(id) on delete cascade NOT NULL,
    link_id  bigint references links(id) on delete cascade NOT NULL,
    created_at timestamp NOT NULL DEFAULT NOW(),
    primary key (book_id, link_id)
);

create table books_pictures (
     book_id bigint references books(id) on delete cascade NOT NULL,
     link_id  bigint references links(id) on delete cascade NOT NULL,
     created_at timestamp NOT NULL DEFAULT NOW(),
     primary key (book_id, link_id)
);