package io.tweetable.ddd.core

/** 集約を表す抽象
  */
trait Aggregate[ID <: Identifier[?], AR <: AggregateRootEntity[ID]]

sealed trait Animal
case class Cat() extends Animal
case class Dog() extends Animal

def animalConv[X <: Animal](x: X) =
  x match
    case _: Cat => "cat"
    case _: Dog => 42
