CREATE TABLE token
(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    token VARCHAR(100) NOT NULL,
    expiration TIMESTAMP NOT NULL ,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_token_user_id FOREIGN KEY (user_id) REFERENCES users(id)
)