CREATE TABLE user
(
    id           INT                 NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email        VARCHAR(128) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    first_name   VARCHAR(32)         NOT NULL,
    last_name    VARCHAR(32)         NOT NULL,
    created_date DATE                NOT NULL,
    updated_date DATE                NOT NULL
);


CREATE TABLE role
(
    id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(32),
    name        VARCHAR(32)
);

CREATE TABLE user_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

ALTER TABLE user_roles
    ADD CONSTRAINT FKh8ciramu9cc9q3qcqiv4ue8a6
        FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE;

ALTER TABLE user_roles
    ADD CONSTRAINT FKhfh9dx7w3ubf1co1vdev94g3f
        FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;