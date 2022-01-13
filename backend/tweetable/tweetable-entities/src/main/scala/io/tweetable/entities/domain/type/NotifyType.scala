package io.tweetable.entities.domain.`type`

import cats.syntax.option.*

object NotifyType:
  enum NotifyType:
    case Followed
    case Favorited
    case ReTweeted

  given Conversion[NotifyType, String] = _ match
    case NotifyType.Followed  => "followed"
    case NotifyType.Favorited => "favorited"
    case NotifyType.ReTweeted => "retweeted"

  given Conversion[String, Option[NotifyType]] = _ match
    case "followed"  => NotifyType.Followed.some
    case "favorited" => NotifyType.Favorited.some
    case "retweeted" => NotifyType.ReTweeted.some
    case _           => None
