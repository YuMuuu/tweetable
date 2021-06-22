package io.tweetable.ddd.core


// Fの制約は充分か？
//todo: Fの制約をいい感じに足す

/**
 * Repositoryを表す抽象
 */
abstract class Repository[F[_] , AE <: AggregateRootEntity] {
  def findById(id: AE#ID): F[Option[AE]]

  def store(entity: AE): F[Unit]

  def delete(id: AE#ID): F[Unit]
}
