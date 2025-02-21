create role modern_user with login password 'letmein';

create database modern_db with owner modern_user;

create table greetings (
    greeting_id bigint generated always as identity not null,
    greeting varchar(120),
    constraint pk_greeting primary key(greeting_id)
);

insert into greetings(greeting) values('Hello, World!');
