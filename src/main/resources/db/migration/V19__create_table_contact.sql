CREATE TABLE contact(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    email VARCHAR(150) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    date_of_posting TIMESTAMP NOT NULL,
    reply_date TIMESTAMP
);