package io.tweetable.repository

import cats.effect.kernel.MonadCancel
import io.tweetable.ddd.core.Repository
import io.tweetable.entities.entity.Tweet
import io.tweetable.entities.entity.Tweet.TweetId

abstract class TweetRepository[
    F[_]: [F[_]] =>> MonadCancel[F, Throwable]
] extends Repository[F, TweetId, Tweet]
