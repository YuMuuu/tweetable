package io.tweetable.ddd.core

/** 集約を表す抽象
  */
trait Aggregate[ID <: Identifier[?], AR <: AggregateRootEntity[ID]]
