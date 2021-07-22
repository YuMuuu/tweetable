package io.tweetable.entities.domain.`type`

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.MaxSize
import eu.timepit.refined.refineV

object String140:
  type String140 = String Refined MaxSize[140]

  given Conversion[String140, String] with
    def apply(string140: String140): String = string140.toString

  //refineVの型引数の指定方法が微妙
  given Conversion[String, Option[String140]] with
    def apply(string: String): Option[String140] =
      refineV[MaxSize[140]](string).toOption
