CREATE TABLE
  guild_article_category
(
  guild_id  INT NOT NULL REFERENCES guilds(id) ,
  article_category_id  INT NOT NULL REFERENCES article_categories(id)
);
