drop table if exists clients;
create table clients(
    id int AUTO_INCREMENT PRIMARY KEY,
    email varchar(30),
    first_name varchar(30),
    last_name varchar(30)
);