INSERT INTO role (id, description, name)
VALUES ('1', 'Admin', 'ROLE_ADMIN');
INSERT INTO role (id, description, name)
VALUES ('2', 'User create', 'ROLE_USER_CREATE');
INSERT INTO role (id, description, name)
VALUES ('3', 'User update', 'ROLE_USER_UPDATE');
INSERT INTO role (id, description, name)
VALUES ('4', 'User', 'ROLE_USER');

-- password = dragonball -> bncrypt
INSERT INTO user (id, email, password, first_name, last_name, created_date, updated_date)
VALUES ('1', 'goku@gmail.com', '$2y$12$s9uXdKgvgZhpVQbVm1zxyOLeOJ3gTdrRQ7npt9yzNGCLykU2oI.9O', 'Goku', 'Akira',
        to_date('11-11-19', 'DD-MM-RR'), to_date('11-11-19', 'DD-MM-RR'));
INSERT INTO user (id, email, password, first_name, last_name, created_date, updated_date)
VALUES ('2', 'yagami.light@deathnote.com', '$2y$12$s9uXdKgvgZhpVQbVm1zxyOLeOJ3gTdrRQ7npt9yzNGCLykU2oI.9O', 'Yagami',
        'Light', to_date('11-11-19', 'DD-MM-RR'), to_date('11-11-19', 'DD-MM-RR'));

INSERT INTO user_roles (user_id, role_id)
VALUES ('1', '1');
INSERT INTO user_roles (user_id, role_id)
VALUES ('1', '2');
INSERT INTO user_roles (user_id, role_id)
VALUES ('2', '2');