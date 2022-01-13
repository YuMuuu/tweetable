package io.tweetable.repository

import cats.effect.kernel.MonadCancel
import io.tweetable.ddd.core.Repository
import io.tweetable.entities.entity.Notify
import io.tweetable.entities.entity.Notify.NotifyId

abstract class NotifyRepository[
    F[_]: [F[_]] =>> MonadCancel[F, Throwable]
] extends Repository[F, NotifyId, Notify]
