DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user_roles;

CREATE TABLE user (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(250) UNIQUE NOT NULL,
  password VARCHAR(250) NOT NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL
);

create table role (
id int not null auto_increment PRIMARY KEY,
description varchar(255),
name varchar(255)
 );

 create table user_roles (
user_id int not null,
role_id int not null,
primary key (user_id, role_id)
);

alter table user_roles add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references role(id);
alter table user_roles add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references user(id);

INSERT INTO role(description,name) values ('Admin', 'ADMIN');
INSERT INTO role(description,name) values ('User', 'USER');





