package io.tweetable

import doobie.ConnectionIO
import doobie.implicits.*
import doobie.implicits.toSqlInterpolator
import io.tweetable.TweetRepositoryHelper.TweetRow
import io.tweetable.ddd.core.LongId
import io.tweetable.entities.domain.`type`.String140.String140
import io.tweetable.entities.domain.`type`.TweetType.TweetType
import io.tweetable.entities.entity
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.repository.TweetRepository
import scala.language.implicitConversions
import cats.free.Free
import doobie.util.query.Query0

import doobie.*
import doobie.implicits.*
import doobie.util.ExecutionContexts
import cats.*
import cats.data.*
import cats.effect.*
import cats.implicits.*

class TweetRepositoryImpl extends TweetRepository[ConnectionIO]:
  override def findById(id: TweetId): ConnectionIO[Option[Tweet]] =
    sql"SELECT id, text, user_id, tweet_Type, re_tweet_tweet_id, reply_tweet_tweet_id FROM tweets WHERE id = ${id.value}"
      .query[TweetRow]
      .option
      .map(
        _.flatMap(_.toTweet())
      ) //memo: queryのresultがnoneの時とrowからentityに変換が失敗した時のnoneがflatしているので同じになっている。微妙かも

  override def store(entity: Tweet): ConnectionIO[Tweet] =
    // (if entity.id != LongId.notAssigned then
    //    sql"""INSERT INTO tweets (text, user_id, tweet_Type, re_tweet_tweet_id, reply_tweet_tweet_id) VALUES(${entity.text.value}, ${entity.userId.value}, ${entity.tweetType
    //      .value()}, ${entity.reTweetTweetId.map(
    //      _.value
    //    )}, ${entity.replyTweetTweetId.map(_.value)})"""
    //  else
    //    sql"""UPDATE tweets SET id = ${entity.id.value} text = ${entity.text.value} user_id=${entity.userId.value} re_tweet_tweet_id=${entity.reTweetTweetId
    //      .map(_.value)} reply_tweet_tweet_id=${entity.replyTweetTweetId.map(
    //      _.value
    //    )}"""
    // ).update.run.map(_id => entity.copy(id = LongId(_id)))
    // memo: Tweetは更新する事がないのでinsertのみの実装
    sql"""INSERT INTO tweets (text, user_id, tweet_Type, re_tweet_tweet_id, reply_tweet_tweet_id) VALUES(${entity.text.value}, ${entity.userId.value}, ${entity.tweetType
      .value()}, ${entity.reTweetTweetId.map(
      _.value
    )}, ${entity.replyTweetTweetId.map(_.value)})""".update.run.map(_id =>
      entity.copy(id = LongId(_id))
    )

  override def delete(id: TweetId): ConnectionIO[Unit] = ???

object TweetRepositoryHelper:
  case class TweetRow(
      id: Int,
      text: String,
      user_id: Int,
      tweet_type: String,
      re_tweet_tweet_id: Option[Int],
      reply_tweet_tweet_id: Option[Int]
  ):
    def toTweet(): Option[Tweet] =
      import scala.language.implicitConversions
      import io.tweetable.entities.domain.`type`.String140.given_Conversion_String_Option
      import io.tweetable.entities.domain.`type`.TweetType.given_Conversion_String_Option

      //memo: Conversionで暗黙の型変換をする時に変換後の型を明示しないとコンパイルできない
      val string140: Option[String140] = text
      val _tweetType: Option[TweetType] = tweet_type

      for
        s <- string140
        tt <- _tweetType
      yield Tweet(
        id = LongId(id),
        text = s,
        userId = LongId(user_id),
        tweetType = tt,
        reTweetTweetId = re_tweet_tweet_id.map(LongId(_)),
        replyTweetTweetId = reply_tweet_tweet_id.map(LongId(_))
      )

  given Conversion[Tweet, TweetRow] with
    def apply(tweet: Tweet): TweetRow =
      TweetRow(
        tweet.id.value.toInt,
        tweet.text.value,
        tweet.userId.value.toInt,
        tweet.tweetType,
        tweet.reTweetTweetId.map(_.value.toInt),
        tweet.replyTweetTweetId.map(_.value.toInt)
      )
