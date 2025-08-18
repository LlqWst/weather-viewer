CREATE SCHEMA weather_viewer;
SET SCHEMA weather_viewer;

CREATE TABLE weather_viewer.users (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      login VARCHAR(100) NOT NULL UNIQUE,
                                      password VARCHAR(100) NOT NULL
);

CREATE TABLE weather_viewer.locations (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          name VARCHAR(100) NOT NULL,
                                          user_id INT NOT NULL,
                                          latitude NUMERIC(9, 6) CHECK (latitude BETWEEN -90 AND 90),
                                          longitude NUMERIC(10, 6) CHECK (longitude BETWEEN -180 AND 180),
                                          CONSTRAINT fk_user1 FOREIGN KEY (user_id)
                                              REFERENCES weather_viewer.users (id) ON DELETE CASCADE
);

CREATE TABLE weather_viewer.sessions (
                                         id UUID PRIMARY KEY,
                                         user_id INT NOT NULL,
                                         expires_at TIMESTAMP,
                                         CONSTRAINT fk_user2 FOREIGN KEY (user_id)
                                             REFERENCES weather_viewer.users (id) ON DELETE CASCADE
);

CREATE INDEX idx_location_user_id ON weather_viewer.locations (user_id);
CREATE INDEX idx_sessions_user_id ON weather_viewer.sessions (user_id);