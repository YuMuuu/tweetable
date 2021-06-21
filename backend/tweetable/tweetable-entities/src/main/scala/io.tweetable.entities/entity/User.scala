package io.tweetable.entities.entity

import cats.effect.IO
import io.tweetable.ddd.core.typeclass.{FlatMap, Functor}
import io.tweetable.ddd.core.{Aggregate, AggregateRootEntity, LongId, Repository}
import io.tweetable.entities.entity.User.UserId

import scala.concurrent.{ExecutionContext, Future}


object User {
  type UserId = LongId
}


case class User(id: UserId, name: String) extends AggregateRootEntity {
  override type ID = UserId
}


//usecase abst sample
trait UserCrudUseCase extends Aggregate[User] {
  def create(user: User): IO[Unit]
  def findById(userId: UserId): IO[Option[User]]
  def update(user: User): IO[User]
  def delete(user: User): IO[Unit]
}

//usecase impl sample
class UserCrudUseCaseImpl extends  UserCrudUseCase {
  override def create(user: User): IO[Unit] = ???

  override def findById(userId: UserId): IO[Option[User]] = ???

  override def update(user: User): IO[User] = ???

  override def delete(user: User): IO[Unit] = ???
}

object FlatMapOps {
  implicit def catsIOFlatMap[A[_]](implicit  fm: FlatMap[A]) = new FlatMap[cats.effect.IO] {
    override def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = fa flatMap f
  }

  implicit def futureFlatMap[A[_]](implicit fm: FlatMap[A], ec: ExecutionContext) = new FlatMap[scala.concurrent.Future] {
    override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B]  = fa flatMap f
  }
}

object FunctorOps {
  implicit def catsIOFunctor[A[_]](implicit fc: Functor[A]) = new Functor[cats.effect.IO] {
    override def map[A, B](fa: IO[A])(f: A => B): IO[B] = fa map f
  }

  implicit def futureFunctor[A[_]](implicit fc: Functor[A], ec: ExecutionContext) = new Functor[scala.concurrent.Future] {
    override def map[A, B](fa: Future[A])(f: A => B): Future[B] = fa map f
  }
}

////repository abst
//trait UserRepository[U <: AggregateRootEntity, F[U] <: Functor[F] with FlatMap[F]] extends Repository[F, U] {
//  override def findById(id: U#ID): F[Option[U]]
//  override def store(entity: U): F[Unit]
//  override def delete(id: U#ID): F[Unit]
//}
//
//import FunctorOps.catsIOFunctor
//import FlatMapOps.catsIOFlatMap
//class UserRepositoryImpl[U <: User with AggregateRootEntity, IO[U] <: Functor[IO] with FlatMap[IO]]
//  extends UserRepository[User, IO[U]] {
//
//  override def findById(id: User#ID): IO[Option[User]] = ???
//  override def store(entity: User): IO[Unit] = ???
//  override def delete(id:  User#ID): IO[Unit] = ???
//}
