package ir.itstar.playground

import scalaz.Monoid


object ScalazPlayGround extends App {

  implicit val StringMonoid: Monoid[String] = new Monoid[String] {
    val zero: String = ""

    override def append(f1: String, f2: => String): String = s"$f1 $f2"
  }

  implicit val IntMonoid: Monoid[Int] = new Monoid[Int] {
    val zero: Int = 0

    override def append(f1: Int, f2: => Int): Int = f1 + f2


  }

  trait MonoidOp[A] {
    def F: Monoid[A]

    def value: A

    def |+|(a2: A): A = F.append(value, a2)
  }

  implicit class StringM(val value: String) extends MonoidOp[String] {
    val F: Monoid[String] = implicitly[Monoid[String]]
  }

  implicit class IntM(val value: Int) extends MonoidOp[Int] {
    val F: Monoid[Int] = implicitly[Monoid[Int]]
  }

  println("hello" |+| "world")
  println(4 |+| 7)

  val intMonoid = implicitly[Monoid[Int]]


}
