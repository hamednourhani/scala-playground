package ir.itstar.playground.dsl

import scala.language.higherKinds

trait NaturalTransformation[F[_], G[_]] {
  def transform[A](f: F[A]): G[A]
}