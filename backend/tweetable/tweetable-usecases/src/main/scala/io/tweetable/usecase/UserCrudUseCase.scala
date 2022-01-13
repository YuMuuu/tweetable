package io.tweetable.usecase

import cats.effect.kernel.MonadCancel
import cats.effect.{IO, Resource}
import cats.free.Free
import doobie.ConnectionIO
import doobie.syntax.ConnectionIOOps
import doobie.free.connection
import io.tweetable.ddd.core.Aggregate
import io.tweetable.ddd.core.typeclass.{Transactable, Transactor}
import io.tweetable.entities.entity.User
import io.tweetable.entities.entity.User.UserId
import io.tweetable.repository.UserRepository

/** User集約のcommand操作用のusecase
  */
trait UserCrudUseCase extends Aggregate[UserId, User]:
  def create(user: User): IO[Unit]
  def findById(userId: UserId): IO[Option[User]]
  def update(user: User): IO[User]
  def delete(userId: UserId): IO[Unit]

class UserCrudUseCaseImpl(
    userRepository: UserRepository[ConnectionIO],
    transactor: Resource[IO, Transactor[ConnectionIO, IO]]
)(
    implicit ev1: MonadCancel[ConnectionIO, Throwable],
    ev2: Transactable[ConnectionIO]
) extends UserCrudUseCase:
  override def create(user: User): IO[Unit] =
    val cio: Free[connection.ConnectionOp, Unit] =
      for _ <- userRepository.store(user)
      yield ()
    for _ <- transactor.use(xa => Transactable[ConnectionIO].transact(xa)(cio))
    //      _ <- transactor.use(xa => cio.transact(xa)) //doobieのTransactorをそのまま使ったほうが記述が短くなる
    yield ()

  override def findById(userId: UserId): IO[Option[User]] =
    val cio =
      for maybeUser <- userRepository.findById(userId)
      yield maybeUser
    for
      maybeUser <- transactor.use(xa =>
        Transactable[ConnectionIO].transact(xa)(cio)
      )
    yield maybeUser

  override def update(user: User): IO[User] =
    // ドメインサービスでupdate可能なuserEntityを作ったとする
    val cio =
      for user <- userRepository.update(user)
      yield user
    for
      user <- transactor.use(xa => Transactable[ConnectionIO].transact(xa)(cio))
    yield user

  override def delete(userId: UserId): IO[Unit] =
    val cio =
      for _ <- userRepository.delete(userId)
      yield ()
    for _ <- transactor.use(xa => Transactable[ConnectionIO].transact(xa)(cio))
    yield ()
