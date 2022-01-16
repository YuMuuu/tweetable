package io.tweetable.entities.entity

import io.tweetable.ddd.core.{
  AggregateRootCheck,
  AggregateRootEntity,
  Entity,
  LongId
}
import io.tweetable.entities.entity.User.UserId

object Account

/** アカウント情報を示すEntity
  */
//todo: email, passwardの型を明確にする
//memo: パスワードは平文で保存する
//memo: メアドとパスパードのみでのログインを想定
case class Account(
    id: UserId,
    email: String,
    passward: String
) extends Entity[UserId]
