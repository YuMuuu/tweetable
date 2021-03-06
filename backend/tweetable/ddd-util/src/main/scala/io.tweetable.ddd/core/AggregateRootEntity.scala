package io.tweetable.ddd.core

/** 集約ルートを表す抽象
  */
trait AggregateRootEntity[ID <: Identifier[?]] extends Entity[ID]
