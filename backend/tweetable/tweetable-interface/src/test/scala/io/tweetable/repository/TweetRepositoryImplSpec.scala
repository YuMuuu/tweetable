package io.tweetable.repository

import doobie.ConnectionIO
import org.scalatest.flatspec.AsyncFlatSpec
import scala.concurrent.Future
import cats.effect.{IO, Resource}
import cats.effect.kernel.Resource
import io.tweetable.TweetRepositoryImpl
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.ddd.core.LongId
import io.tweetable.ddd.core.typeclass.{Transactable, Transactor}
import doobie.*
import doobie.implicits.*
import cats.effect.*
import cats.implicits.*
import doobie.util.ExecutionContexts
import doobie.syntax.ConnectionIOOps
import scala.language.implicitConversions
import cats.effect.Resource
import io.tweetable.ddd.core.typeclass.DoobieTransactable
import io.tweetable.ddd.core.typeclass.HikariTransactor as HT
import io.tweetable.ddd.core.typeclass.DoobieTransactor as DT
import doobie.hikari.*
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import cats.effect.unsafe.implicits.global
import doobie.util.transactor.Transactor.Aux
import io.tweetable.entities.domain.`type`.TweetType.TweetType
import io.tweetable.entities.domain.`type`.String140

// doobieを使っているのでRepository層で実際にqueryを叩くtestとは別にqurey自体が正しいかを試すtestが欲しい
class TweetRepositoryImplSpec extends AsyncFlatSpec:
  // ------
  // 共通モジュールにまとめる
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
    LongId(1),
    String140.unsafeString140("hello"),
    LongId(1),
    TweetType.NormalTweet,
    None,
    None
  )
  // ------

  "tweet.findby" should "tweetが見つからなかった場合結果がNoneになる" in {
    val cio = tweetRepositoryImpl.findById(LongId(1))
    val result = transactor.use(Transactable[ConnectionIO].transact(_)(cio))
    result.unsafeToFuture().map(ot => assert(ot === None))
  }

  it should "tweetが見つかった場合決結果を返す" in {
    val text = "hello"
    val tweetType = "tweet"
    val cio = for
      _ <-
        sql"INSERT INTO tweets (id, text, user_id, tweet_Type) VALUES(1, ${text}, 1, ${tweetType})".update.run
      maybeTweet <- tweetRepositoryImpl.findById(LongId(1))
    yield maybeTweet
    val result = transactor.use(Transactable[ConnectionIO].transact(_)(cio))
    result.unsafeToFuture().map(ot => assert(ot === tweet))
  }
