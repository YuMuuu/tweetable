package io.tweetable.repository

import doobie.ConnectionIO
import org.scalatest.flatspec.AsyncFlatSpec
import cats.effect.{IO, Resource}
import io.tweetable.TweetRepositoryImpl
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.ddd.core.LongId
import io.tweetable.ddd.core.typeclass.{Transactable, Transactor}
import doobie.*
import doobie.implicits.*
import scala.language.implicitConversions
import cats.effect.Resource
import io.tweetable.ddd.core.typeclass.DoobieTransactable
import io.tweetable.ddd.core.typeclass.HikariTransactor as HT
import doobie.hikari.*
import cats.effect.unsafe.implicits.global
import io.tweetable.entities.domain.`type`.TweetType.TweetType
import io.tweetable.entities.domain.`type`.String140

// doobieを使っているのでRepository層で実際にqueryを叩くtestとは別にqurey自体が正しいかを試すtestが欲しい
class TweetRepositoryImplSpec extends AsyncFlatSpec:

  // ------
  // todo: 共通モジュールにまとめる
  given ev2: Transactable[ConnectionIO] = DoobieTransactable.doobieTansactable
  val transactor: Resource[IO, Transactor[ConnectionIO, IO]] =
    for
      ec <- ExecutionContexts.fixedThreadPool[IO](32)
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = "org.postgresql.Driver",
        url = "jdbc:postgresql://localhost:5432/postgres",
        user = "root",
        pass = "root",
        connectEC = ec
      )
      ht = HT(xa) //memo: given conversionが上手く効かないので明示的に変換する
    yield ht

  val tweetRepositoryImpl = TweetRepositoryImpl()
  val tweet = Tweet(
    LongId(2),
    String140.unsafeString140("hello"),
    LongId(1),
    TweetType.NormalTweet,
    None,
    None
  )

  "tweet.findById" should "tweetが見つからなかった場合結果がNoneになる" in {
    val cio = tweetRepositoryImpl.findById(LongId(1))
    val result = transactor.use(Transactable[ConnectionIO].transact(_)(cio))
    result.unsafeToFuture().map(ot => assert(ot === None))
  }

  it should "tweetが見つかった場合決結果を返す" in {
    val cio = for
      _ <-
        sql"INSERT INTO tweets (id, text, user_id, tweet_Type) VALUES(2, 'hello', 1, 'tweet')".update.run
      maybeTweet <- tweetRepositoryImpl.findById(LongId(2))
      _ <- sql"DELETE FROM tweets WHERE id = 2".update.run
    yield maybeTweet
    val result = transactor.use(Transactable[ConnectionIO].transact(_)(cio))

    result.unsafeToFuture().map(ot => assert(ot === Some(tweet)))
  }

  "tweet.store" should "normalツイートを登録する" in {
    val normalTweet = Tweet(
      LongId.notAssigned,
      String140.unsafeString140("hello"),
      LongId(1),
      TweetType.NormalTweet,
      None,
      None
    )
    val cio = for
      storedTweet <- tweetRepositoryImpl.store(normalTweet)
      maybeTweet <- tweetRepositoryImpl.findById(storedTweet.id)
      _ <- sql"DELETE FROM tweets WHERE id = ${storedTweet.id.value}".update.run
    yield storedTweet

    val result = transactor.use(Transactable[ConnectionIO].transact(_)(cio))

    result
      .unsafeToFuture()
      .map(ot =>
        assert(
          (
            ot.text,
            ot.userId,
            ot.tweetType,
            ot.reTweetTweetId,
            ot.replyTweetTweetId
          ) === (normalTweet.text, normalTweet.userId, normalTweet.tweetType, normalTweet.reTweetTweetId, normalTweet.replyTweetTweetId)
        )
        assert(ot.id != LongId.notAssigned)
      )

  }

  it should "リツイートを登録する" in {
    val reTweet = Tweet(
      LongId.notAssigned,
      String140.unsafeString140("hello"),
      LongId(1),
      TweetType.ReTweet,
      Some(tweet.id),
      None
    )
    val cio = for
      _ <- tweetRepositoryImpl.store(tweet)
      storedTweet <- tweetRepositoryImpl.store(reTweet)
      maybeTweet <- tweetRepositoryImpl.findById(storedTweet.id)
      _ <- sql"DELETE FROM tweets WHERE id = ${storedTweet.id.value}".update.run
      _ <- sql"DELETE FROM tweets WHERE id = ${tweet.id.value}".update.run
    yield storedTweet

    val result = transactor.use(Transactable[ConnectionIO].transact(_)(cio))

    result
      .unsafeToFuture()
      .map(ot =>
        assert(
          (
            ot.text,
            ot.userId,
            ot.tweetType,
            ot.reTweetTweetId,
            ot.replyTweetTweetId
          ) === (reTweet.text, reTweet.userId, reTweet.tweetType, reTweet.reTweetTweetId, reTweet.replyTweetTweetId)
        )
        assert(ot.id != LongId.notAssigned)
      )

    // "tweet.delete" should "ツイートを削除できる" in {
    // }

  }
