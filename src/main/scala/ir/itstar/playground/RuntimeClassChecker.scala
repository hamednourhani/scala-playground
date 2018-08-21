package ir.itstar.playground

import scala.annotation.StaticAnnotation

object RuntimeClassChecker extends App {

  println("runtime class checker")


  class TN(name: String) extends StaticAnnotation

  class Id extends StaticAnnotation

  sealed trait TestTrait


  private case class TestClass(@Id id: String, name: String) extends TestTrait

  final case class FinalClass(id: String) extends TestTrait

  println(classOf[TestTrait])
  println(classOf[TestClass])

  classOf[TestClass].getConstructors.foreach(c => println(c.getName))


  assert(classOf[TestClass].getModifiers == 9)
  assert(classOf[FinalClass].getModifiers == 25)

}
