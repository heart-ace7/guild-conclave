CREATE TABLE
  guild_article
(
  guild_id  INT NOT NULL REFERENCES guilds(id),
  article_id  INT NOT NULL REFERENCES articles(id)
);
