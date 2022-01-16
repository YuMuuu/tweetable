package io.tweetable.entities.entity

import io.tweetable.ddd.core.{AggregateRootCheck, Entity, LongId}
import io.tweetable.entities.entity.User.UserId

case class Follow(
    id: UserId, //idがidというfiled名に限定されるのは微妙かも？
    followList: List[UserId]
) extends Entity[UserId]
