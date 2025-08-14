CREATE SCHEMA IF NOT EXISTS weather_viewer;

CREATE TABLE IF NOT EXISTS weather_viewer.users (
    id SERIAL PRIMARY KEY,
    login VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS weather_viewer.location (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    user_id INT NOT NULL,
    latitude NUMERIC NOT NULL,
    longitude NUMERIC NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES weather_viewer.users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS weather_viewer.sessions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    expires_at TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id)
        REFERENCES weather_viewer.users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_location_user_id ON weather_viewer.location (user_id);
CREATE INDEX IF NOT EXISTS idx_sessions_user_id ON weather_viewer.sessions (user_id);