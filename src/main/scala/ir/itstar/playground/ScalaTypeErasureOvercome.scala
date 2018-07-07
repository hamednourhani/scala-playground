package ir.itstar.playground


import scala.reflect._
import scala.reflect.runtime.universe._

object ScalaTypeErasureOvercome extends App {

  /**
    * ClassTag cannot recognize Type of Higher-Type so use TypeTag or WeakTypeTag
    *
    * compiler will build a ClassTag of type T in compile type if any is available and
    *
    * will use it in pattern matching ClassTag(_:T) =>
    *
    */

  object Extractor {
    def extract[T](list: List[Any])(implicit tag: ClassTag[T]): List[T] = list.map {
      case t: T => Some(t)
      case _    => None
    }.filter(_.isDefined).map(_.get)

  }

  object Factory {
    def arrayOfTypeT[T: ClassTag](elems: T*): Array[T] = elems.toArray[T]

  }

  val typedArrayData = Factory.arrayOfTypeT(22, 34)

  typedArrayData.foreach(println)

  val list = List("String", 22, true)

  println(Extractor.extract[String](list))


  /**
    * TypeTag cannot recognize actual type of value in runtime
    *
    * note : TypeTag cannot recognize abstract type
    *
    */


  object Recognizer {
    def recognize[T](x: T)(implicit tag: TypeTag[T]): String =
      tag.tpe match {
        case TypeRef(utype, usymbol, args) =>
          List(utype, usymbol, args).mkString("\n")
      }

    def recognizeAbstractType[T](x: T)(implicit tag: WeakTypeTag[T]): String = {
      tag.tpe match {
        case TypeRef(utype, usymbol, args) =>
          List(utype, usymbol, tag).mkString("\n")
      }
    }
  }

  val listInList = List(List("s", "t"), List(1, 2))

  val result = Recognizer.recognize(listInList)

  println(result)

  abstract class AbClass[T]

  val t: AbClass[String] = null

  val abResult = Recognizer.recognizeAbstractType(t)

  println(abResult)


  /**
    * scala.reflect helper methods to create wrapper type classes
    */

  val ct = classTag[String]
  val tt = typeTag[List[Int]]
  val wtt = weakTypeOf[List[Int]]

  val array = ct.newArray(3)
  array.update(0, "s")
  array.update(1, "h")

  println(array.mkString(","))
  println(tt.tpe)
  println(wtt.equals(tt))

  /**
    * reference : https://medium.com/@sinisalouc/overcoming-type-erasure-in-scala-8f2422070d20
    */
}
