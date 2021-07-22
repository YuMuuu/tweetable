package io.tweetable.entities.entity

import io.tweetable.entities.domain.`type`.String140.String140
import io.tweetable.ddd.core.{AggregateRootEntity, LongId, AggregateRootCheck}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User.UserId


object Tweet {
  type TweetId = LongId

  //静的assersion的な
  private[this] val ev = summon[AggregateRootCheck[Tweet]]
}


case class Tweet(
    id: TweetId,
    text: String140,
    userId: UserId
) extends AggregateRootEntity[TweetId]
