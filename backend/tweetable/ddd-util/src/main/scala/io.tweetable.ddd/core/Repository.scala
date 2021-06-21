package io.tweetable.ddd.core

import io.tweetable.ddd.core.typeclass.{FlatMap, Functor}

// Fの制約は充分か？
// 足りなかったら足していく
trait Repository[F[_] <: Functor[F] with FlatMap[F], AE <: AggregateRootEntity] {
  def findById(id: AE#ID): F[Option[AE]]

  def store(entity: AE): F[Unit]

  def delete(id: AE#ID): F[Unit]
}
