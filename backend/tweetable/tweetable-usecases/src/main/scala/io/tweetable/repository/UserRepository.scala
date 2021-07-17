package io.tweetable.repository

import io.tweetable.ddd.core.{AggregateRootEntity, Repository}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User


// repositoryの抽象でUがUserだという事を明示したい。Implの方で型を注入するのは変な感じがする
abstract class UserRepository[F[U], U <: AggregateRootEntity] extends Repository[F, U] {
  def findByTweetId(id: TweetId): F[Option[U]]
  def update(user: User): F[U]
}