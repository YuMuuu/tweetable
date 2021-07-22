package io.tweetable.ddd.core

import scala.util.Try
/*
Identifierの具象
UUID型を持つIdentifierの抽象
 */

import UUID._
case class UUID(value: Raw) extends Identifier[Raw] {}

object UUID:
  type Raw = java.util.UUID

  def generate(): UUID = UUID(java.util.UUID.randomUUID())
  def fromString(string: String): Option[UUID] = Try(java.util.UUID.fromString(string)).toOption.map(UUID(_))
