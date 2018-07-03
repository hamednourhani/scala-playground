package ir.itstar.playground.dsl


trait Executor[F[_]] {
  def exec[A](f: F[A]): A
}