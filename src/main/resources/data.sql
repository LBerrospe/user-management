DROP TABLE IF EXISTS user;

CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  email VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);

INSERT INTO user (email, password) VALUES
  ('goku@gmail.com', '123'),
  ('goten@gmail.com', '123'),
  ('gohan@gmail.com', '123');