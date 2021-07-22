package io.tweetable.ddd.core.typeclass

import cats.effect.kernel.MonadCancel
import cats.effect.IO
import cats.~>

import scala.language.implicitConversions
import scala.languageFeature.higherKinds

trait Transactor[M[_], N[_]]:
  def trans(using ev: MonadCancel[N, Throwable]): M ~> N

trait Transactable[M[_]]:
  def transact[A](transactor: Transactor[M, IO])(m: M[A])(using
      ev: MonadCancel[IO, Throwable]
  ): IO[A]

object Transactable:
  // scala 2.13
  //  def apply[M[_]](implicit ev: Transactable[M[*]]): Transactable[M[*]] = implicitly[Transactable[M[*]]]

  // dotty
//    def apply[M[_]](using ev: Transactable[({type L[F[_]] = M[F]})#L]): Transactable[({type R[F[_]] = M[F]})#R] =
//    implicitly[Transactable[({type R[F[_]] = M[F]})#R]]
  def apply[F[_]](implicit ev: Transactable[F]): Transactable[F] =
    implicitly[Transactable[F]]

//命名微妙
//object DoobieTransactable {
//  implicit def toTransactable(implicit tr: Transactable[doobie.ConnectionIO]) = new Transactable[doobie.ConnectionIO] {
//    override def transact[A](transactor: Transactor[doobie.ConnectionIO, IO])(m: doobie.ConnectionIO[A])(implicit ev: MonadCancel[IO, Throwable]): IO[A] =
//      transactor.trans.apply(m) //ntの適用法もっとかっこいい書き方がありそう
//  }
//
//}

//[error] -- Error: ******/Transactor.scala:24:62
//[error] 24 |    def apply[M[_]](using ev: Transactable[({type L[F[_]] = M[F]})#L]): Transactable[({type R[F[_]] = M[F]})#R] =
//[error]    |                                                              ^
//[error]    |               Type argument F does not have the same kind as its bound
//[error] -- Error: ******/Transactor.scala:24:104
//[error] 24 |    def apply[M[_]](using ev: Transactable[({type L[F[_]] = M[F]})#L]): Transactable[({type R[F[_]] = M[F]})#R] =
//[error]    |                                                                                                        ^
//[error]    |               Type argument F does not have the same kind as its bound
//[error] -- Error: ******/Transactor.scala:25:47
//[error] 25 |    implicitly[Transactable[({type R[F[_]] = M[F]})#R]]
//[error]    |                                               ^
//[error]    |               Type argument F does not have the same kind as its bound

class DoobieTransactor[N[_]](underlying: doobie.util.transactor.Transactor[N])
    extends Transactor[doobie.ConnectionIO, N]:
  override def trans(implicit
      ev: MonadCancel[N, Throwable]
  ): doobie.ConnectionIO ~> N = underlying.trans
