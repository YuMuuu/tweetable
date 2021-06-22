package io.tweetable.ddd.core.typeclass

import cats.effect.{Bracket, IO}
import cats.~>

import scala.language.implicitConversions
import scala.languageFeature.higherKinds

trait Transactor[M[_], N[_]] {
  def trans(implicit ev: Bracket[N, Throwable]): M ~> N
}

trait Transactable[M[_]] {
  def transact[A](transactor: Transactor[M, IO])(m: M[A])(implicit ev: Bracket[IO, Throwable]): IO[A]
}

object Transactable {
  def apply[M[?]](implicit ev: Transactable[M[?]]): Transactable[M[?]] = implicitly[Transactable[M[?]]]
}

//命名微妙
//object DoobieTransactable {
//  implicit def toTransactable(implicit tr: Transactable[doobie.ConnectionIO]) = new Transactable[doobie.ConnectionIO] {
//    override def transact[A](transactor: Transactor[doobie.ConnectionIO, IO])(m: doobie.ConnectionIO[A])(implicit ev: Bracket[IO, Throwable]): IO[A] =
//      transactor.trans.apply(m) //ntの適用法もっとかっこいい書き方がありそう
//  }
//
//}

class DoobieTransactor[N[_]](underlying: doobie.util.transactor.Transactor[N]) extends Transactor[doobie.ConnectionIO, N] {
  override def trans(implicit ev: Bracket[N, Throwable]): doobie.ConnectionIO ~> N = underlying.trans
}
