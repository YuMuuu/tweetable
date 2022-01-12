package io.tweetable.entities.entity

import io.tweetable.entities.domain.`type`.String140.String140
import io.tweetable.ddd.core.{AggregateRootCheck, AggregateRootEntity, LongId}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.TweetType.TweetType
import io.tweetable.entities.entity.User.UserId

object Tweet:
  type TweetId = LongId

  //静的assersion的な
  private[this] val ev = summon[AggregateRootCheck[Tweet]]

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

/** TweetTypeを表すADT NormalTweet: 通常のツイート ReTweet: リツイート
  */
object TweetType:
  enum TweetType:
    case NormalTweet
    case ReTweet

  given Conversion[TweetType, String] with
    def apply(tweetType: TweetType): String = tweetType match
      case TweetType.NormalTweet => "tweet"
      case TweetType.ReTweet     => "retweet"

  given Conversion[String, Option[TweetType]] with
    def apply(string: String): Option[TweetType] = string match
      case "tweet"   => Some(TweetType.NormalTweet)
      case "retweet" => Some(TweetType.ReTweet)
      case _         => None
