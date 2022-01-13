package io.tweetable.entities.domain.`type`

import cats.syntax.option.*

/** TweetTypeを表すADT NormalTweet: 通常のツイート, ReTweet: リツイート
  */
object TweetType:
  enum TweetType:
    case NormalTweet
    case ReTweet

  given Conversion[TweetType, String] = _ match
    case TweetType.NormalTweet => "tweet"
    case TweetType.ReTweet     => "retweet"

  given Conversion[String, Option[TweetType]] = _ match
    case "tweet"   => TweetType.NormalTweet.some
    case "retweet" => TweetType.ReTweet.some
    case _         => None
