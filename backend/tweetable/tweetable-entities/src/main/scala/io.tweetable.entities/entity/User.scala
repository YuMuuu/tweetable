package io.tweetable.entities.entity

import io.tweetable.ddd.core.{Entity, LongId}
import io.tweetable.entities.entity.User.UserId

object User {
  type UserId = LongId
}

case class User(id: UserId, name: String) extends Entity {
  override type ID = UserId
}


