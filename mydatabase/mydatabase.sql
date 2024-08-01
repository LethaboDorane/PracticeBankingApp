create database if not exists mzansi_bank_system;

use mzansi_bank_system;

create table if not exists users (
    user_id int auto_increment primary key,
    fname varchar(50) not null check (length(fname) >= 2),
    surname varchar(50) not null check (length(surname) >= 2),
    username varchar(50) unique not null check (length(username) >= 7),
    password varchar(50) not null check (length(password) >= 10)
);

create table if not exists bank_accounts (
    account_id varchar(10) primary key,
    username varchar(50) references users(username),
    account_type ENUM('Savings', 'Cheque', 'Current', 'Fixed-Deposit') not null,
    amount decimal(10,2) default 0.00,
    foreign key (username) references users(username)
);

select * from users;

select * from bank_accounts;