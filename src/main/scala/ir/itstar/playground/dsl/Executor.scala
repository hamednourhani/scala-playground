package ir.itstar.playground.dsl

import scala.language.higherKinds


trait Executor[F[_]] {
  def exec[A](f: F[A]): A
}