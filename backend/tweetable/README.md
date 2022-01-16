# DDDをモデリングする

`/backend/tweetable/ddd-util` に実装がある

このライブラリを使わなくなった時になるべく依存や継承を消せばそのままコンパイルが通るように設計した


機能としては
- 集約ルートEntityからではないとEntityを永続化できないようにしている
  - Repositoryの実装の型パラで制約を課している
- 集約ルートEntityは集約ルートEntityを持てない
  - AggregateRootCheckでコンパイル時に落とせる

 などがある

 Transactableの集約があり直接DDDとは関係ないが、これを利用することによって`トランザクション`を集約と意識する事ができる

 非CQRS + ES ならcommand側だけこのライブラリを利用してquery側はパフォーマンスの良いクエリを書けば利用できる（と思う）

 CQRSはあんまり理解していないので対応しているか分からない。メッセージキュー周りと結果整合がなぜできるのかが分からない...

 https://github.com/j5ik2o/scala-ddd-base
 このrepositoryのより厳格な抽象のみを提供するイメージ
# scala3
`ddd-util`、またサンプルアプリではscala3での機能を利用している
- macro
- typed lambda
- 新しいimplicit
- inline base syntax
- scala3用のscalafmtの設定
など 

# DDDをモデリングしたライブラリを利用してサンプルアプリを作る（WIP）
ツイッターっぽいSNSのバックエンド。
## 実装済み

- TweetEntity, UserEntityなど
  - 集約ルートEntityの実装
- FollowEntity, FavoriteEntityなど
  - Entityの実装
- TweetRepository
  - Tweet集約の永続化を行う
- TweetCrudUseCase
  - DDDに置けるサービスの中で特にReoisitoryの実装を直接叩けば良いものの実装
- TweetService
  - DDDに置けるサービスの中で他の集約の値やバリデーションなど複雑な処理が必要なものの実装
  - httpclientのからこのクラスのメソッドを呼ぶイメージ
### マイグレーション
`evolutions`を利用

- flywayっぽいOSSのマイグレーションツール。playframeworkで利用されている。無料だとflywayはロールバックできないのでこっちの方が良いかも？
## 未実装
### http client
tweetable-driverに実装予定

### サーバ実行するための色々
- ログイン情報を保持するcookieの管理など

###　その他色々


# テストの実行方法
TweetRepositoryImplのtestなのであんまりおもしろくない...
```
docker compose up -d
cd backend/tweetable/
sbt evolutions/run #migration
sbt test #testの実行
```
