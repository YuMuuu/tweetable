-- Users schema

-- !Ups

CREATE TABLE users (
  id  SERIAL  NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  fullname VARCHAR(255) NOT NULL,
  isAdmin BOOLEAN NOT NULL,
  PRIMARY KEY (id)
);

-- !Downs

DROP TABLE users;
