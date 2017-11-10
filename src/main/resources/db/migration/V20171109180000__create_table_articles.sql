CREATE TABLE
  articles
(
  id         SERIAL PRIMARY KEY,
  title      VARCHAR(255) NOT NULL,
  content    TEXT         NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP,
  category_id INT NOT NULL REFERENCES article_categories(id),
  priority   INT NOT NULL
);
