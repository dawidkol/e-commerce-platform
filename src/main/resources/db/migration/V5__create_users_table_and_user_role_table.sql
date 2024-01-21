CREATE TABLE user_role
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    name        VARCHAR(20) UNIQUE,
    description VARCHAR(300)
);

CREATE TABLE users
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(75),
    last_name  VARCHAR(75),
    email      VARCHAR(75) UNIQUE,
    password   VARCHAR(200),
    role_id    BIGINT,
    CONSTRAINT fk_user_user_role FOREIGN KEY (role_id) REFERENCES user_role (id)
);
