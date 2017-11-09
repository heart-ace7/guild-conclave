CREATE TABLE
  article_categories
(
  id        SERIAL PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  parent_id INT REFERENCES article_categories(id),
  priority  INT NOT NULL
);
