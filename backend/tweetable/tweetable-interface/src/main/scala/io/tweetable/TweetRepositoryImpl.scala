package io.tweetable

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator
import io.tweetable.TweetRepositoryHelper.TweetRow
import io.tweetable.ddd.core.LongId
import io.tweetable.entities.domain.`type`.String140.String140
import io.tweetable.entities.entity
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.TweetType.TweetType
import io.tweetable.repository.TweetRepository
import scala.Conversion

class TweetRepositoryImpl extends TweetRepository[ConnectionIO]:
  override def findById(id: TweetId): ConnectionIO[Option[Tweet]] =
    //db設計をしていないのでqueryは適当
    sql"select id, text, userId from tweet where id = ${id}"
      .query[TweetRow]
      .unique
      .map(_.toTweet())

  override def store(entity: Tweet): ConnectionIO[Unit] = ???

  override def delete(id: TweetId): ConnectionIO[Unit] = ???

object TweetRepositoryHelper:
  case class TweetRow(
      id: Long,
      text: String,
      userId: Long,
      tweetType: String,
      reTweetTweetId: Option[Long],
      replyTweetTweetId: Option[Long]
  ):
    def toTweet(): Option[Tweet] =
      import scala.language.implicitConversions
      import io.tweetable.entities.domain.`type`.String140.given_Conversion_String_Option
      import io.tweetable.entities.entity.TweetType.given_Conversion_String_Option
      // import io.tweetable.entities.domain.`type`.String140._ memo: //アンスコimportができない

      //memo: Conversionで暗黙の型変換をする時に変換後の型を明示しないとコンパイルできない
      val string140: Option[String140] = text
      val _tweetType: Option[TweetType] = tweetType

      for
        s <- string140
        tt <- _tweetType
      yield Tweet(
        id = LongId(id),
        text = s,
        userId = LongId(userId),
        tweetType = tt,
        reTweetTweetId = reTweetTweetId.map(LongId(_)),
        replyTweetTweetId = replyTweetTweetId.map(LongId(_))
      )

  given Conversion[Tweet, TweetRow] with
    def apply(tweet: Tweet): TweetRow =
      TweetRow(
        tweet.id.value,
        tweet.text.value,
        tweet.userId.value,
        tweet.tweetType,
        tweet.reTweetTweetId.map(_.value),
        tweet.replyTweetTweetId.map(_.value)
      )
