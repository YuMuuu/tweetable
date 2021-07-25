package io.tweetable.entities.entity

import io.tweetable.ddd.core.{AggregateRootCheck, Entity, LongId}
import io.tweetable.entities.entity.User.UserId

case class FollowerList(
    id: UserId,
    followerList: List[UserId]
)
