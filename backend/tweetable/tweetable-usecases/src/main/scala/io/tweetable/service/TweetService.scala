package io.tweetable.service

import io.tweetable.usecase.TweetCrudUseCase
import io.tweetable.entities.entity.Tweet
import cats.effect.IO
import io.tweetable.entities.domain.`type`.TweetType.TweetType
import io.tweetable.entities.entity.Tweet.TweetId

class TweetService(tweetCrudUseCase: TweetCrudUseCase):

  def createNormalTweet(tweet: Tweet): IO[Tweet] =
    assert(tweet.tweetType == TweetType.NormalTweet)
    //assertではなくcats.Validatedを利用する
    tweetCrudUseCase.create(tweet)
  def createReTweet(tweet: Tweet): IO[Tweet] =
    assert(tweet.tweetType == TweetType.ReTweet)
    tweetCrudUseCase.create(tweet)

  def delete(tweetId: TweetId): IO[Unit] =
    tweetCrudUseCase.delete(tweetId)
