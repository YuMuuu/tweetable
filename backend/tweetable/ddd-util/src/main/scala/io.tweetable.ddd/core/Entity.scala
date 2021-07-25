package io.tweetable.ddd.core

/** Entityを表す抽象
  */
trait Entity[ID <: Identifier[_]]:
  val id: ID

  def sameIdentityAs(that: Entity[ID]): Boolean =
    this.getClass == that.getClass && this.id == that.id
