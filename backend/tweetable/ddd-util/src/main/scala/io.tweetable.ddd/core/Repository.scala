package io.tweetable.ddd.core

import cats.effect.kernel.MonadCancel

import scala.concurrent.ExecutionContext


/**
 * Repositoryを表す抽象
 */

abstract class Repository[F[_]: ({type L[F[_]] = MonadCancel[F, Throwable]})#L , AE <: AggregateRootEntity] {
  type ID = AE#ID

  def findById(id: ID): F[Option[AE]]

  def store(entity: AE): F[Unit]

  def delete(id: ID): F[Unit]
}


//class Rep(str: String)(using ec: ExecutionContext) {
//  val a = 42
//}

class Rep2(str: String) {
  val a = 42
}