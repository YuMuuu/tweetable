package io.tweetable.ddd.core

/** 集約を表す抽象
  */
//todo:
// この形だとひとつの集約にひとつの集約ルートがあるということしかしめせていない
// 集約の中にEntityなり値オブジェクトが含まれているということを表現できていない
// 現状ではpackageを分けるぐらいしか方法はない？
trait Aggregate[ID <: Identifier[_], AR <: AggregateRootEntity[ID]]
