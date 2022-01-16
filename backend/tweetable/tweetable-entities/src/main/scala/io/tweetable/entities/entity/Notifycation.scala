package io.tweetable.entities.entity

import io.tweetable.ddd.core.LongId
import io.tweetable.ddd.core.AggregateRootCheck
import io.tweetable.entities.entity.Notification.NotificationId
import io.tweetable.ddd.core.AggregateRootEntity
import io.tweetable.entities.entity.User.UserId
import io.tweetable.entities.entity.Favorite.FavoriteId
import io.tweetable.entities.entity.Tweet.TweetId
import cats.syntax.option.*
import scala.quoted.FromExpr.NoneFromExpr
import io.tweetable.entities.domain.`type`.NotifyType.NotifyType

object Notification:
  type NotificationId = LongId

  //静的assersion的な
  private[this] val ev = summon[AggregateRootCheck[Notification]]

  def factoryFollowedNotify(userId: UserId, followId: UserId): Notification =
    Notification(
      id = LongId.notAssigned,
      userId = userId,
      notifyType = NotifyType.Followed,
      followId = followId.some,
      favoriteId = None,
      reTweetId = None
    )

  def factoryFavoritedNotify(
      userId: UserId,
      favoriteId: FavoriteId
  ): Notification =
    Notification(
      id = LongId.notAssigned,
      userId = userId,
      notifyType = NotifyType.Favorited,
      followId = None,
      favoriteId = favoriteId.some,
      reTweetId = None
    )

  def factoryReTweetedNotify(userId: UserId, reTweetId: TweetId): Notification =
    Notification(
      id = LongId.notAssigned,
      userId = userId,
      notifyType = NotifyType.ReTweeted,
      followId = None,
      favoriteId = None,
      reTweetId = reTweetId.some
    )

case class Notification(
    id: NotificationId,
    userId: UserId,
    notifyType: NotifyType,
    followId: Option[UserId],
    favoriteId: Option[FavoriteId],
    reTweetId: Option[TweetId]
) extends AggregateRootEntity[NotificationId]
//memo: assertionを作る
