-- Users schema

-- !Ups

CREATE TABLE users (
  id  SERIAL  NOT NULL,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

-- !Downs

DROP TABLE users;
