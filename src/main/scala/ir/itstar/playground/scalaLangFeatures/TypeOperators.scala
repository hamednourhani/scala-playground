package ir.itstar.playground.scalaLangFeatures

object TypeOperators extends App {

  trait Parent[T] {
    val value: T

    case class Child[F](name: T,id:F)

  }


  val parent: Parent[String] = new Parent[String] {
    override val value: String = "parent"
  }

  val child: Parent[Int]#Child[String] = new Parent[Int] {
    override val value: Int = 3
  }.Child(2,"child")

  println(parent)
  println(child)


}
