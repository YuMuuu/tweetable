package io.tweetable.repository

import cats.effect.Bracket
import io.tweetable.ddd.core.{AggregateRootEntity, Repository}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User


abstract class UserRepository[F[_]: Bracket[*[_], Throwable]] extends Repository[F, User] {
  def findByTweetId(id: TweetId): F[Option[User]]
  def update(user: User): F[User]
}

