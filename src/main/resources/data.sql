INSERT INTO role (description,name) VALUES ('Admin', 'ADMIN');
INSERT INTO role (description,name) VALUES ('User', 'USER');

-- password = dragonball -> bncrypt
INSERT INTO user (id, email, password, first_name, last_name) VALUES ('1', 'goku@gmail.com', '$2y$12$s9uXdKgvgZhpVQbVm1zxyOLeOJ3gTdrRQ7npt9yzNGCLykU2oI.9O', 'Goku', 'Akira');

INSERT INTO user_roles (user_id, role_id) VALUES ('1', '1');