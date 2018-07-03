package ir.itstar.playground.dsl

trait NaturalTransformation[F[_], G[_]] {
  def transform[A](f: F[A]): G[A]
}