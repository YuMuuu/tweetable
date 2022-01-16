package io.tweetable.entities.entity

import io.tweetable.entities.domain.`type`.String140.String140
import io.tweetable.entities.domain.`type`.TweetType.TweetType
import io.tweetable.ddd.core.{AggregateRootCheck, AggregateRootEntity, LongId}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User.UserId
import scala.language.implicitConversions
import cats.syntax.option.*

object Tweet:
  type TweetId = LongId

  //静的assersion的な
  private[this] val ev = summon[AggregateRootCheck[Tweet]]

  //memo: TweetTypeごとにfactoryを用意する
  def apply(
      text: String,
      userId: UserId,
      tweetType: String,
      reTweetTweetId: Option[TweetId],
      replyTweetTweetId: Option[TweetId]
  ): Option[Tweet] =
    import io.tweetable.entities.domain.`type`.String140.given_Conversion_String_Option
    import io.tweetable.entities.domain.`type`.TweetType.given_Conversion_String_Option

    val string140: Option[String140] = text
    val tt: Option[TweetType] = tweetType

    for
      _text <- string140
      _tweetType <- tt
    yield Tweet(
      id = LongId.notAssigned,
      text = _text,
      userId = userId,
      tweetType = _tweetType,
      reTweetTweetId = reTweetTweetId,
      replyTweetTweetId = replyTweetTweetId
    )

/** Tweetを示すEntity
  */
case class Tweet(
    id: TweetId,
    text: String140,
    userId: UserId,
    tweetType: TweetType,
    reTweetTweetId: Option[TweetId],
    replyTweetTweetId: Option[TweetId]
) extends AggregateRootEntity[TweetId] {
  //memo: tweetType, ReTweetTweetId, ReplyTweetTweetId のassertionを定義する
}
