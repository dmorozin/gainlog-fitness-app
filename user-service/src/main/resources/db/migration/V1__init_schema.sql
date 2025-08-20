CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(100) NOT NULL,
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_email UNIQUE (email),
    CONSTRAINT uq_username UNIQUE (username)
);
CREATE INDEX ix_users_email ON users (email);

CREATE TABLE roles
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_name UNIQUE (name)
);
CREATE INDEX ix_roles_name ON roles (name);

CREATE TABLE user_roles
(
    user_id    BIGSERIAL REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
    role_id    BIGSERIAL REFERENCES roles (id) ON UPDATE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id)
);
