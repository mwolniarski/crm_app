drop table if exists leads;
create table leads(
    id int AUTO_INCREMENT PRIMARY KEY,
    email varchar(30),
    first_name varchar(30),
    last_name varchar(30)
);