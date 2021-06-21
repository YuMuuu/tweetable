package io.tweetable.ddd.core.typeclass

import cats.effect.IO

import scala.concurrent.{ExecutionContext, Future}

trait FlatMap[F[_]] {
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
}


//implicit def optionShow[A](implicit sa: Show[A]) = new Show[Option[A]] {
//  def show(oa: Option[A]): String = oa match {
//  case None => "None"
//  case Some(a) => "Some("+ sa.show(a) + ")"
//  }
//  }


object FlatMapOps {
  implicit def catsIOFlatMap[A[_]](implicit  fm: FlatMap[A]) = new FlatMap[cats.effect.IO] {
    override def flatMap[A, B](fa: IO[A])(f: A => IO[B]): IO[B] = fa flatMap f
  }

  implicit def futureFlatMap[A[_]](implicit fm: FlatMap[A], ec: ExecutionContext) = new FlatMap[scala.concurrent.Future] {
    override def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B]  = fa flatMap f
  }
}

