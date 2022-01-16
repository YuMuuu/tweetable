package io.tweetable.ddd.core.typeclass

import cats.effect.kernel.MonadCancel
import cats.effect.IO
import cats.~>

import scala.language.implicitConversions
import scala.languageFeature.higherKinds

trait Transactor[M[_], N[_]]:
  def trans(using ev: MonadCancel[N, Throwable]): M ~> N

trait Transactable[M[_]]:
  def transact[A](transactor: Transactor[M, IO])(m: M[A])(
      using ev: MonadCancel[IO, Throwable]
  ): IO[A]

object Transactable:
  def apply[F[_]](implicit ev: Transactable[F]): Transactable[F] =
    implicitly[Transactable[F]]

// 実装の詳細がここにあるのは微妙...
class DoobieTransactor[N[_]](underlying: doobie.util.transactor.Transactor[N])
    extends Transactor[doobie.ConnectionIO, N]:
  override def trans(
      using ev: MonadCancel[N, Throwable]
  ): doobie.ConnectionIO ~> N = underlying.trans

class HikariTransactor[N[_]](underlying: doobie.hikari.HikariTransactor[N])
    extends Transactor[doobie.ConnectionIO, N]:
  override def trans(
      using ev: MonadCancel[N, Throwable]
  ): doobie.ConnectionIO ~> N = underlying.trans

object DoobieTransactable:
  // given doobieTansactable(
  //     using tr: Transactable[doobie.ConnectionIO]
  // ): Transactable[doobie.ConnectionIO] = new Transactable[doobie.ConnectionIO]:
  //   override def transact[A](transactor: Transactor[doobie.ConnectionIO, IO])(
  //       m: doobie.ConnectionIO[A]
  //   )(using ev: MonadCancel[IO, Throwable]): IO[A] = transactor.trans(m)
  val doobieTansactable = new Transactable[doobie.ConnectionIO]:
    def transact[A](transactor: Transactor[doobie.ConnectionIO, IO])(
        m: doobie.ConnectionIO[A]
    )(using ev: MonadCancel[IO, Throwable]): IO[A] = transactor.trans.apply(m)
