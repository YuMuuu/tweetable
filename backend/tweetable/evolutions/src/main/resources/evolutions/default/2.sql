-- Users schema

-- !Ups


CREATE TABLE tweets (
  id  SERIAL  NOT NULL,
  text VARCHAR(255) NOT NULL,
  user_id INTEGER NOT NULL,
  tweet_type VARCHAR(255) NOT NULL,
  re_tweet_tweet_id INTEGER,
  reply_tweet_tweet_id INTEGER,
  PRIMARY KEY (id)
);

CREATE TABLE accounts (
  user_id  INTEGER  NOT NULL,
  email VARCHAR(255) NOT NULL,
  passward VARCHAR(255) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE favorites (
  id  SERIAL  NOT NULL,
  user_id INTEGER NOT NULL,
  tweet_id VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE follows (
  user_id INTEGER NOT NULL,
  follow_id  INTEGER NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE notifications (
  id  SERIAL  NOT NULL,
  user_id INTEGER NOT NULL,
  notify_type VARCHAR(255) NOT NULL,
  follow_id INTEGER,
  favorite_id INTEGER,
  re_tweet_id INTEGER,
  PRIMARY KEY (id) 
);


-- !Downs


DROP TABLE notifications;
DROP TABLE follows;
DROP TABLE favorites;
DROP TABLE accounts;
DROP TABLE tweets;

