package io.tweetable.entities.entity

import io.tweetable.ddd.core.{
  AggregateRootCheck,
  AggregateRootEntity,
  Entity,
  LongId
}
import io.tweetable.entities.entity.Account.AccountId

object Account:
  type AccountId = LongId

/** アカウント情報を示すEntity
  */
//todo: email, passwardの型を明確にする
//memo: メアドとパスパードのみでのログインを想定
case class Account(
    id: AccountId,
    email: String,
    passward: String
) extends Entity[AccountId]
