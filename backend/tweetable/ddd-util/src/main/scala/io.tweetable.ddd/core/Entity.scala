package io.tweetable.ddd.core

/**
 * Entityを表す抽象
 *
 */
trait Entity {
  type ID <: Identifier[_]

  val id: ID

  //  def sameIdentityAs(that: Entity): Boolean = {
  //    this.getClass == that.getClass && this.id == that.id
  //  }
}
