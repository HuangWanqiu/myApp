use accompany;
create table user(
user_id int primary key not null auto_increment,
user_name varchar(100),
email varchar(100) not null unique,
phone_number varchar(20) not null  unique,
head_portrait_path varchar(100),
user_password varchar(100), 
motto varchar(100));

create table user_message(
user_id int,
send_time datetime,
content varchar(1000),
audio_path varchar(100)
);

create table robot_message(
user_id int,
robot_id int,
send_time datetime,
content varchar(1000),
audio_path varchar(100)
);


create table robot(
robot_id int primary key not null auto_increment,
robot_name varchar(100),
robot_setting varchar(1000)
);





