package io.tweetable.usecase

import cats.effect.kernel.MonadCancel
import cats.effect.{IO, Resource}
import doobie.ConnectionIO
import doobie.implicits.*
import doobie.syntax.ConnectionIOOps
import io.tweetable.ddd.core.Aggregate
import io.tweetable.ddd.core.typeclass.{Transactable, Transactor}
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.repository.TweetRepository
import io.tweetable.repository.NotifyRepository
import io.tweetable.entities.entity.Notification
import io.tweetable.entities.domain.`type`.TweetType.TweetType
import doobie.free.Embedded.Connection

import cats.*
import cats.effect.*
import cats.implicits.*
import cats.syntax.option.*
import cats.syntax.OptionOps
import cats.data.OptionT

trait TweetCrudUseCase extends Aggregate[TweetId, Tweet]:
  def create(tweet: Tweet): IO[Tweet]

  def findById(tweetId: TweetId): IO[Option[Tweet]]

  //  def update(tweet: Tweet): IO[Tweet]
  //tweetは更新する事が無いので一旦コメントアウト、likeがtweet集約に含まれるなら更新することもあるかも？

  def delete(tweetId: TweetId): IO[Unit]

class TweetCrudUseCaseImpl(
    tweetRepository: TweetRepository[ConnectionIO],
    notifyRepository: NotifyRepository[ConnectionIO],
    transactor: Resource[IO, Transactor[ConnectionIO, IO]]
)(
    implicit ev1: MonadCancel[ConnectionIO, Throwable],
    ev2: Transactable[ConnectionIO]
) extends TweetCrudUseCase:
  override def create(tweet: Tweet): IO[Tweet] =
    val cio =
      for
        storedTweet <- tweetRepository.store(tweet)
        notify = tweet.tweetType match
          // ReTweetだったらNotifyRepositoryにstoreする
          case TweetType.ReTweet =>
            {
              // reTweetの場合はuserIdとReTweetIdは必ず取れるのでここで頑張らないといけないのはなにかおかしい。
              for
                reTweetId <- OptionT.fromOption[ConnectionIO](
                  tweet.reTweetTweetId
                )
                reTweet <- OptionT(tweetRepository.findById(reTweetId))
                n = Notification.factoryReTweetedNotify(
                  reTweet.userId,
                  reTweetId
                )
                _ <- OptionT.liftF(notifyRepository.store(n))
              yield ()
            }.value.map(_ => Applicative[ConnectionIO].unit)

          //memo: ConnectionIO.unit にしたいがどこにimplicitが定義されてるのかよく分からなかった。
          case _ => Applicative[ConnectionIO].unit
        _ <- notify
      yield storedTweet

    for
      storedTweet <- transactor.use(xa =>
        Transactable[ConnectionIO].transact(xa)(cio)
      )
    yield storedTweet

  override def findById(tweetId: TweetId): IO[Option[Tweet]] =
    val cio =
      for maybeTweet <- tweetRepository.findById(tweetId)
      yield maybeTweet
    for
      maybeTweet <- transactor.use(xa =>
        Transactable[ConnectionIO].transact(xa)(cio)
      )
    yield maybeTweet

  override def delete(tweetId: TweetId): IO[Unit] =
    val cio =
      for _ <- tweetRepository.delete(tweetId)
      yield ()
    for _ <- transactor.use(xa => Transactable[ConnectionIO].transact(xa)(cio))
    yield ()
