DDL SQL for healthChecker, table host and table health

create table host ( id bigint(20) not null primary key auto_increment, ip tinyblob, name varchar(255), registered_time varchar(255), updated_time varchar(255) );

create table health (id bigint(20) not null primary key auto_increment, connection bit(1), updated_time varchar(255));
