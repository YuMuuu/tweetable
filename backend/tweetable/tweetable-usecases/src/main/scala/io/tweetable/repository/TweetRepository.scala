package io.tweetable.repository

import cats.effect.kernel.MonadCancel
import io.tweetable.ddd.core.Repository
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId


abstract class TweetRepository[
    F[_]: ({type L[F[_]] = MonadCancel[F, Throwable]})#L
] extends Repository[F, TweetId, Tweet]