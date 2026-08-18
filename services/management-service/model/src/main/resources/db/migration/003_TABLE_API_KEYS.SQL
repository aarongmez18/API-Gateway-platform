
CREATE TABLE api_keys (
    id BIGSERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL,
    key_hash VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_api_key_client
        FOREIGN KEY (client_id)
        REFERENCES clients(id)
);