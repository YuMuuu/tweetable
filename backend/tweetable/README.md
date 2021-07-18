cats + typesafeによせたtwitter closeのバックエンドアプリ

最初はCQRS+非ESで作ってやる気が残っていたらESで作り直す
- ESをやるにしても非akkaで実装する(akkaはscala Futureなため)

follow entity

```scala
//entity
case class FollowList(
id: long,
owner: User,
followee_users: List[UserId] //ownerがfollowしているUser一覧
)

case class FollowerList(
id: long,

)
```