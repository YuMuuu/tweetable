package io.tweetable.entities.entity

import io.tweetable.ddd.core.LongId
import io.tweetable.entities.entity.Favorite.FavoriteId
import io.tweetable.entities.entity.Tweet.TweetId
import io.tweetable.ddd.core.Entity

object Favorite:
  type FavoriteId = LongId

/** ふぁぼを示すEntity
  */
case class Favorite(
    id: FavoriteId,
    tweet: TweetId
) extends Entity[FavoriteId]
