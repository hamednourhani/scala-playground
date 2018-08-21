package ir.itstar.playground

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

object ScalaReflectRunner extends App {
  println("scala reflect runner")

  def add[T: TypeTag]: Unit = {

    extract(typeOf[T])
  }

  def extract(t: Type): Any = {
    if (!t.typeSymbol.isFinal) {
      val map: Map[(AnyRef with universe.SymbolApi)#NameType, (AnyRef with universe.SymbolApi)#NameType] = t.members.filter(!_.isMethod).map { t =>
        val k: universe.Type = t.typeSignature
        val kType = k.typeSymbol.name
        extract(k)

        t.name -> kType
      }.toMap

      val params = map.foldLeft[String]("")((acc, tu) => s"$acc ${tu._1}:${tu._2}")

      println(s"${t.typeSymbol.name}($params)")
    }
  }

  add[Parent]

}

sealed class Test(t: String, k: Int, p: List[String])

sealed trait TestTrait

class Parent(test: Test) extends TestTrait