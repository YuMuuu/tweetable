package io.tweetable.ddd.core

/*
Identifierの具象
Long型を持つIdentifierの抽象
type UserId = LongId などと type alias を作成して利用する
  example:
    type UserId = LongId
    val userId: UserId = IntId(1000L)
    val notAssignedUserId: UserId = IntId.notAssigned


    https://docs.scala-lang.org/scala3/reference/other-new-features/opaques.html
    これを使うとboxing無しにvalue objectを作れる？？
 */

case class LongId(value: Long) extends Identifier[Long]:
  def isAssigned: Boolean = value.!=(-1L)

object LongId:
  def notAssigned: LongId = LongId(-1L)
