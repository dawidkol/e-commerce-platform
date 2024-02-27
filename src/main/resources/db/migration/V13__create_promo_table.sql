CREATE TABLE promo(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    discount_percent BIGINT NOT NULL,
    active_start TIMESTAMP NOT NULL,
    active_end TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL,
    usage_count BIGINT NOT NULL,
    max_usage_count BIGINT NOT NULL
)