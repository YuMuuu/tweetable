package io.tweetable.entities.entity

import io.tweetable.ddd.core.{Aggregate, Entity, LongId}
import io.tweetable.entities.entity.User.UserId

//更新、取得
case class FolloweeList(
    id: UserId, //idがidというfiled名に限定されるのは微妙かも？
    followeeList: List[UserId]
) extends Entity {
  override type ID = UserId
}

//取得のみ
//query側で利用する？
case class FollowerList(
    id: UserId,
    followerList: List[UserId]
)

//CREATE TABLE follow_list (
// userId int,
// follow_userId int
//);

