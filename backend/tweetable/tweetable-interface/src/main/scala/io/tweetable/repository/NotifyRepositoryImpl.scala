package io.tweetable.repository

import doobie.ConnectionIO
import io.tweetable.entities.entity.Notification
import io.tweetable.entities.entity.Notification.NotificationId
import io.tweetable.repository.NotifyRepository

class NotifyRepositoryImpl extends NotifyRepository[ConnectionIO]:
  override def delete(id: NotificationId): ConnectionIO[Unit] = ???
  override def findById(
      id: NotificationId
  ): ConnectionIO[Option[Notification]] = ???
  override def store(entity: Notification): ConnectionIO[Notification] = ???
