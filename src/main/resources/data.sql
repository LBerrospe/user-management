DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(250) UNIQUE NOT NULL,
  password VARCHAR(250) NOT NULL,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL
);

INSERT INTO user (email, password, first_name, last_name) VALUES
  ('goku@gmail.com', '123','goku','akira'),
  ('goten@gmail.com', '123','goten','akira'),
  ('gohan@gmail.com', '123','gohan','akira');