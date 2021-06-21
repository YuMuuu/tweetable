package io.tweetable.entities.entity

import io.tweetable.ddd.core.{AggregateRootEntity, LongId}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User.UserId


object Tweet {
  type TweetId = LongId
}


case class Tweet(id: TweetId, text: String, userId: UserId) extends AggregateRootEntity {
  override type ID = TweetId
}