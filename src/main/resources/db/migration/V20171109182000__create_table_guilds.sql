CREATE TABLE
  guilds
(
  id         SERIAL PRIMARY KEY,
  game_id    INT NOT NULL REFERENCES games(id),
  title      VARCHAR(255) NOT NULL,
  priority   INT NOT NULL
);
