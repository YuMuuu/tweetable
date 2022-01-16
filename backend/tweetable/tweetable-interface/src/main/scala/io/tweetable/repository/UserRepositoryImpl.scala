package io.tweetable

import doobie.ConnectionIO
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User
import io.tweetable.entities.entity.User.UserId
import io.tweetable.repository.UserRepository

//実装の方でAREを設定するのは変な感じ
class UserRepositoryImpl extends UserRepository[ConnectionIO]:
  override def findByTweetId(id: TweetId): ConnectionIO[Option[User]] = ???
  override def findById(id: UserId): ConnectionIO[Option[User]] = ???
  override def store(entity: User): ConnectionIO[User] = ???
  override def delete(id: UserId): ConnectionIO[Unit] = ???
