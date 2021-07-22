package io.tweetable.entities.entity

import io.tweetable.ddd.core.{Entity, LongId, AggregateRootCheck}
import io.tweetable.entities.entity.User.UserId

object FolloweeList {}
case class FolloweeList(
    id: UserId, //idがidというfiled名に限定されるのは微妙かも？
    followeeList: List[UserId]
) extends Entity[UserId]

//CREATE TABLE follow_list (
// userId int,
// follow_userId int
//);
