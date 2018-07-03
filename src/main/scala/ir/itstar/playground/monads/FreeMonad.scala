package ir.itstar.playground.monads

import ir.itstar.playground.dsl.{Executor, NaturalTransformation}
import scalaz.Monad

sealed trait Free[F[_], A] {

  def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
    case Pure(a)            => f(a)
    case FlatMap(sub, cont) => FlatMap(sub, cont andThen (_ flatMap f))
  }

  def map[B](f: A => B): Free[F, B] = flatMap(a => Pure(f(a)))
}

case class Pure[F[_], A](a: A) extends Free[F, A]

case class FlatMap[F[_], A, B](sub: F[A], cont: A => Free[F, B]) extends Free[F, B]

object Free {

  implicit def lift[F[_], A](a: F[A]): Free[F, A] = FlatMap(a, Pure.apply)
}

trait FreeFunctions {

  def transformFree[F[_], G[_], A](prg: Free[F, A], nt: NaturalTransformation[F, G])(implicit M: Monad[G]): G[A] = prg match {
    case Pure(a)                               => Monad[G].pure(a)
    case FlatMap(sub, cont: (A => Free[F, A])) =>
      val transformed = nt.transform(sub)
      Monad[G].bind(transformed) { a => transformFree(cont(a), nt) }
  }

  def runFree[F[_], A](prg: Free[F, A], executor: Executor[F]): A = prg match {
    case Pure(a)                               => a
    case FlatMap(sub, cont: (A => Free[F, A])) =>
      val result = executor.exec(sub)
      runFree(cont(result), executor)
  }

}



