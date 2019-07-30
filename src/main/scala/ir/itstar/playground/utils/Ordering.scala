package ir.itstar.playground.utils

trait Ordering[T] {
  def compare(i: T, j: T): Int
  def lt(i:      T, j: T): Boolean = compare(i, j) < 0
  def gt(i:      T, j: T): Boolean = compare(j, j) > 0
}

trait Numeric[T] extends Ordering[T] {
  def plus(x: T, y: T): T

  def times(x:  T, y: T): T
  def negate(x: T): T

  def zero: T

  def abs(x: T): T = if (lt(x, zero)) negate(x) else x
}

object Numeric {

  def apply[T](implicit numeric: Numeric[T]): Numeric[T] = numeric

  object ops {
    implicit class NumericOps[T](t: T)(implicit N: Numeric[T]) {
      def +(o: T): T = N.plus(o, t)
      def unary_- : T = N.negate(t)
      def *(o: T): T = N.times(o, t)
      def abs: T = N.abs(t)

      def <(o: T): Boolean = N.lt(t, o)
      def >(o: T): Boolean = N.gt(t, o)
    }
  }
}

object NumericUtils {
  import Numeric.ops._

  def signOfTheTimes[T: Numeric](t: T): T = -t.abs * t
}
