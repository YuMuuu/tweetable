package io.tweetable.repository

import cats.effect.Bracket
import io.tweetable.ddd.core.Repository
import io.tweetable.entities.entity.Tweet


//abstract class TweetRepository[F[_] : ({type  L[F[_]] = Bracket[F, Throwable]})#L] extends Repository[F, Tweet] {
//}

abstract class TweetRepository[F[_]: Bracket[*[_], Throwable]] extends Repository[F, Tweet] {
}