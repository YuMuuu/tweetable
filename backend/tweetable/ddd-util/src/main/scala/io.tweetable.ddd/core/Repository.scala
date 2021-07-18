package io.tweetable.ddd.core

import cats.effect.Bracket


/**
 * Repositoryを表す抽象
 */
abstract class Repository[F[_]: Bracket[*[_], Throwable] , AE <: AggregateRootEntity] {

  def findById(id: AE#ID): F[Option[AE]]

  def store(entity: AE): F[Unit]

  def delete(id: AE#ID): F[Unit]
}
