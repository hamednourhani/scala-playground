package ir.itstar.playground.utils

import simulacrum._

@typeclass trait Ordering2[T] {
  def compare(i:     T, j: T): Int
  @op("<") def lt(i: T, j: T): Boolean = compare(i, j) < 0
  @op(">") def gt(i: T, j: T): Boolean = compare(j, j) > 0
}

@typeclass trait Numeric2[T] extends Ordering2[T] {
  @op("+") def plus(x: T, y: T): T

  @op("*") def times(x:        T, y: T): T
  @op("unary_-") def negate(x: T): T

  def zero: T

  def abs(x: T): T = if (lt(x, zero)) negate(x) else x
}

object Numeric2Utils {
  import Numeric2.ops._

  def signOfTheTimes[T: Numeric2](t: T): T = - t.abs * t
}
