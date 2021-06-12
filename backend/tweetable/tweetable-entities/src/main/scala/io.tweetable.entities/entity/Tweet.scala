package io.tweetable.entities.entity

import io.tweetable.ddd.core.{Entity, LongId}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User.UserId


object Tweet {
  type TweetId = LongId
}


case class Tweet(id: TweetId, text: String, favorite: Seq[UserId]) extends Entity {
  override type ID = TweetId
}