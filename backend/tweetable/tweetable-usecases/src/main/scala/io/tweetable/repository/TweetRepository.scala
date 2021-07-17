package io.tweetable.repository

import io.tweetable.ddd.core.{AggregateRootEntity, Repository}


abstract class TweetRepository[F[U], U <: AggregateRootEntity] extends Repository[F, U] {
}