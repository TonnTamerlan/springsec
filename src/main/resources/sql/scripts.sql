create table users
(
    username varchar(50)  not null primary key,
    password varchar(500) not null,
    enabled  boolean      not null
);
create table authorities
(
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);

insert into authorities (username, authority)
values ('user', 'read'),
       ('admin', 'admin');


create table customer
(
    id    int          not null auto_increment,
    email varchar(45)  not null,
    pwd   varchar(200) not null,
    role  varchar(45)  not null,
    primary key (id)
);

insert into customer (email, pwd, role)
values ('user@user.com', '{bcrypt}$2a$12$FiYQwK2szpCjQxC6r9An2Onv5RttvGiSzxe12jHBdS02L60DUsdhS', 'read'),
       ('admin@user.com', '{noop}5678', 'admin');