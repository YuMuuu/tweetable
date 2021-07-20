package io.tweetable

import doobie.ConnectionIO
import doobie.implicits.toSqlInterpolator
import io.tweetable.TweetRepositoryHelper.TweetRow
import io.tweetable.ddd.core.LongId
import io.tweetable.entities.domain.`type`.String140.String140
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.repository.TweetRepository
import scala.Conversion

class TweetRepositoryImpl extends TweetRepository[ConnectionIO]{
  override def findById(id: TweetId): ConnectionIO[Option[Tweet]] = {
    //db設計をしていないのでqueryは適当
    sql"select id, text, userId from tweet where id = ${id}".query[TweetRow].unique.map(_.toTweet())
  }

  override def store(entity: Tweet): ConnectionIO[Unit] = ???

  override def delete(id: TweetId): ConnectionIO[Unit] = ???
}

object TweetRepositoryHelper {
  case class TweetRow(
    id: Long,
    text: String,
    userId: Long
  ) {
    def toTweet(): Option[Tweet] = {
      import io.tweetable.entities.domain.`type`.String140.string140ForString
      val string140: Option[String140] = text
      string140.map(s => {
        Tweet(
          id = LongId(id),
          text = s,
          userId = LongId(userId)
        )
      })
    }
  }


  given Conversion[Tweet, TweetRow] with
    def apply(tweet: Tweet): TweetRow = {
          TweetRow(
            tweet.id.value,
            tweet.text.value,
            tweet.userId.value
          )
    }
}
