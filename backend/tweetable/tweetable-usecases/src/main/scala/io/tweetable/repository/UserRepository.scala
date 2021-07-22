package io.tweetable.repository

import cats.effect.kernel.MonadCancel
import io.tweetable.ddd.core.Repository
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User
import io.tweetable.entities.entity.User.UserId


abstract class UserRepository[
    F[_]: ({type L[F[_]] = MonadCancel[F, Throwable]})#L
] extends Repository[F, UserId, User] {
  def findByTweetId(id: TweetId): F[Option[User]]

  def update(user: User): F[User]
}


