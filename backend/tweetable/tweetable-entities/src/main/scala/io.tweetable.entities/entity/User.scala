package io.tweetable.entities.entity

import io.tweetable.ddd.core.{AggregateRootEntity, LongId}
import io.tweetable.entities.entity.User.UserId


object User {
  type UserId = LongId
}


case class User(id: UserId, name: String) extends AggregateRootEntity {
  override type ID = UserId
}