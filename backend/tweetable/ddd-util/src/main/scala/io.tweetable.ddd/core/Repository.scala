package io.tweetable.ddd.core

import cats.effect.kernel.MonadCancel

import scala.concurrent.ExecutionContext


/**
 * Repositoryを表す抽象
 */

abstract class Repository[F[_]: ({type L[F[_]] = MonadCancel[F, Throwable]})#L , AE <: AggregateRootEntity](using val ae: AE) {
  type ID = ae.ID

  def findById(id: ID): F[Option[AE]]

  def store(entity: AE): F[Unit]

  def delete(id: ID): F[Unit]
}

