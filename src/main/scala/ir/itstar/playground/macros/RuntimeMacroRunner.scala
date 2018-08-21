package ir.itstar.playground.macros

import scala.reflect.runtime.universe

object RuntimeMacroRunner extends App {

  import scala.reflect.runtime.universe._

  case class Child(name: String)

  case class TestClass[T](id: String, layer: T, childs: List[Child])

  def classTree[T](t: TestClass[T]): Unit = {



    val mirror = runtimeMirror(t.getClass.getClassLoader)

    val cs = mirror.classSymbol(t.getClass)

    println(cs.name)
    val id = cs.toType.member(TermName("id")).typeSignatureIn(cs.toType)
    println(id)
    val layer = cs.toType.member(TermName("layer")).typeSignatureIn(cs.toType)
    println(layer)
    val childs = cs.toType.member(TermName("childs")).typeSignatureIn(cs.toType)
    println(childs)

  }

  val block = TestClass[String]("id", "layer", List(Child("ali")))

  classTree(block)

}
