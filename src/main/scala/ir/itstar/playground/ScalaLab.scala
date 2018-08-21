package ir.itstar.playground

object ScalaLab extends App {

  implicit class OptionWrapper[T](v: Option[T]) {
    def |(default: T) = v.getOrElse(default)
  }

  implicit class anyWrapper[T](v: T) {
    def some: Option[T] = Some.apply(v)

    def right[U](u: U): Either[T, U] = Right(u)

    def left[U](u: U): Either[U, T] = Left(u)
  }

  def none[Int]: Option[Int] = None


  val s = Some("Hello")
  val k = None

  println(s | "goodBay")
  println(k | "None")

  implicit class BooleanWrapper(con: Boolean) {
    def ?[K](t: K): Option[K] = if (con) Some(t) else None
  }

  println(false ? "hello" | "Some")

  println("hello".some)
  println(none[Int])

  println("error".right(2))
  println(2.left("error"))

}
