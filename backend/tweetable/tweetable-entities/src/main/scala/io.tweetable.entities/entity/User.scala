package io.tweetable.entities.entity

import cats.effect.{Bracket, IO, Resource}
import doobie.ConnectionIO
import io.tweetable.ddd.core.typeclass.{Transactable, Transactor}
import io.tweetable.ddd.core.{Aggregate, AggregateRootEntity, LongId, Repository}
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.entities.entity.User.UserId


object User {
  type UserId = LongId
}


case class User(id: UserId, name: String) extends AggregateRootEntity {
  override type ID = UserId
}


//usecase abst sample
trait UserCrudUseCase extends Aggregate[User] {
  def create(user: User): IO[Unit]
  def findById(userId: UserId): IO[Option[User]]
  def update(user: User): IO[User]
  def delete(user: User): IO[Unit]
}

//usecase impl sample
class UserCrudUseCaseImpl(userRepository: UserRepository[ConnectionIO, User],
                          transactor: Resource[IO, Transactor[ConnectionIO, IO]])
                         (implicit ev1: Bracket[ConnectionIO, Throwable],
                          ev2: Transactable[ConnectionIO])
  extends  UserCrudUseCase {
  override def create(user: User): IO[Unit] = {
    val cio = for {
      _ <- userRepository.store(user)
    } yield ()
    for {
      _ <- transactor.use(xa => Transactable[ConnectionIO].transact(xa)(cio))
//      _ <- transactor.use(xa => cio.transact(xa)) //doobieのTransactorをそのまま使ったほうが記述が短くなる
    } yield ()
  }
  override def findById(userId: UserId): IO[Option[User]] = ???
  override def update(user: User): IO[User] = ???
  override def delete(user: User): IO[Unit] = ???
}


//repository abst
abstract class UserRepository[F[U], U <: AggregateRootEntity] extends Repository[F, U] {
  def findByTweetId(id: TweetId): F[Option[U]]
}


class UserRepositoryImpl extends UserRepository[ConnectionIO, User] {
  override def findByTweetId(id: TweetId): ConnectionIO[Option[User]] = ???
  override def findById(id: UserId): ConnectionIO[Option[User]] = ???
  override def store(entity: User): ConnectionIO[Unit] = ???
  override def delete(id: UserId): ConnectionIO[Unit] = ???
}

