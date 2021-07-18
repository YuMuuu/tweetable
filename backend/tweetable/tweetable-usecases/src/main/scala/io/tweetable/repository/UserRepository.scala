package io.tweetable.repository

import cats.effect.kernel.MonadCancel
import io.tweetable.ddd.core.Repository
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User


abstract class UserRepository[F[_]: ({type L[F[_]] = MonadCancel[F, Throwable]})#L] extends Repository[F, User] {
  def findByTweetId(id: TweetId): F[Option[User]]

  def update(user: User): F[User]
}

