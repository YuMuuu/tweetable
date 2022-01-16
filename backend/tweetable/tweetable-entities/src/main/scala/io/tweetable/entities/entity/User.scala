package io.tweetable.entities.entity

import io.tweetable.ddd.core.{AggregateRootCheck, AggregateRootEntity, LongId}
import io.tweetable.entities.entity.User.UserId

object User:
  type UserId = LongId

  //静的assersion的な
  private[this] val ev = summon[AggregateRootCheck[User]]

case class User(
    id: UserId,
    name: String,
    follow: Follow,
    account: Account
) extends AggregateRootEntity[UserId]
