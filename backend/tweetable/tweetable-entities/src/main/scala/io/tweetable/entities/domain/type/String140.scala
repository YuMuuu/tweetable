package io.tweetable.entities.domain.`type`

import eu.timepit.refined.api.Refined
import eu.timepit.refined.predicates.all.MaxSize
import eu.timepit.refined.refineV

object String140:
  type String140 = String Refined MaxSize[140]

  given Conversion[String140, String] = _.toString

  //refineVの型引数の指定方法が微妙
  given Conversion[String, Option[String140]] =
    refineV[MaxSize[140]](_).toOption

  // test用メソッド
  def unsafeString140(string: String): String140 = refineV[MaxSize[140]](
    string
  ).toOption.getOrElse(throw Exception("failed encode stirng to string140"))
