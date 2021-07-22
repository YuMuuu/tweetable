package io.tweetable.entities.entity

import io.tweetable.ddd.core.{Entity, LongId, AggregateRootCheck}
import io.tweetable.entities.entity.User.UserId

case class FollowerList(
    id: UserId,
    followerList: List[UserId]
)
