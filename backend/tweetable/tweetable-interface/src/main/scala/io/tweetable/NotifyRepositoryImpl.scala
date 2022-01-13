package io.tweetable.repository

import doobie.ConnectionIO
import io.tweetable.entities.entity.Notify
import io.tweetable.entities.entity.Notify.NotifyId
import io.tweetable.repository.NotifyRepository

class NotifyRepositoryImpl extends NotifyRepository[ConnectionIO]:
  override def delete(id: NotifyId): ConnectionIO[Unit] = ???
  override def findById(id: NotifyId): ConnectionIO[Option[Notify]] = ???
  override def store(entity: Notify): ConnectionIO[Unit] = ???
