package io.tweetable.usecase

import cats.effect.kernel.MonadCancel
import cats.effect.{IO, Resource}
import doobie.ConnectionIO
import io.tweetable.ddd.core.Aggregate
import io.tweetable.ddd.core.typeclass.{Transactable, Transactor}
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.repository.TweetRepository

trait TweetCrudUseCase extends Aggregate[TweetId, Tweet]:
  def create(tweet: Tweet): IO[Unit]

  def findById(tweetId: TweetId): IO[Option[Tweet]]

  //  def update(tweet: Tweet): IO[Tweet]
  //tweetは更新する事が無いので一旦コメントアウト、likeがtweet集約に含まれるなら更新することもあるかも？
  def delete(tweetId: TweetId): IO[Unit]

class TweetCrudUseCaseImpl(
    tweetRepository: TweetRepository[ConnectionIO],
    transactor: Resource[IO, Transactor[ConnectionIO, IO]]
)(implicit
    ev1: MonadCancel[ConnectionIO, Throwable],
    ev2: Transactable[ConnectionIO]
) extends TweetCrudUseCase:
  override def create(tweet: Tweet): IO[Unit] =
    val cio =
      for _ <- tweetRepository.store(tweet)
      yield ()
    for _ <- transactor.use(xa => Transactable[ConnectionIO].transact(xa)(cio))
    yield ()

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
