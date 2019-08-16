package ir.itstar.playground.macros

object RuntimeMacroSample extends App {

  import scala.reflect.runtime.{universe => ru}

  val list        = List(1, 2, 3, 4)
  val listTypeTag = getType(list)
  val decls       = listTypeTag.tpe.decls
  println(listTypeTag)
  println(listTypeTag.tpe)

  val memberScope: ru.MemberScope = decls
  println(decls.take(50).mkString("\n"))

  val mirror:          ru.Mirror      = ru.runtimeMirror(getClass.getClassLoader)
  val userClassType:   ru.Type        = mirror.typeOf[User]
  val userSymbol:      ru.Symbol      = userClassType.typeSymbol
  val userClassSymbol: ru.ClassSymbol = userSymbol.asClass
  println(userClassSymbol)

  val rc:      ru.ClassMirror  = mirror.reflectClass(userClassSymbol)
  val contro:  ru.MethodSymbol = ru.typeOf[User].decl(ru.termNames.CONSTRUCTOR).asMethod
  val controM: ru.MethodMirror = rc.reflectConstructor(contro)

  val user1 = controM(23, "hamed")

  val ageTerm: ru.TermSymbol = mirror.typeOf[User].decl(ru.TermName("age")).asTerm

  val user1Mirror:         ru.InstanceMirror = mirror.reflect(user1)
  val user1AgeFieldMirror: ru.FieldMirror    = user1Mirror.reflectField(ageTerm)

  println("change class field by reflection ==========>")
  println(user1AgeFieldMirror.get)
  user1AgeFieldMirror.set(24)
  println(user1AgeFieldMirror.get)
  println("<=========== change class field by reflection")

  val e = new E { type T = String }
  val a = new A { type T = String }
  val b = new B { type T = Int }

  println(s"java-reflection : did a extend e:${e.getClass.isAssignableFrom(a.getClass)}")
  println(s"java-reflection : did b extend e:${e.getClass.isAssignableFrom(b.getClass)}")

  println(s"scala-reflection : did b extend e:${isExtend(a, e)}")

  val intType:    ru.ClassSymbol = ru.definitions.IntClass
  val bookType:   ru.ClassSymbol = ru.definitions.BooleanClass
  val stringType: ru.ClassSymbol = ru.definitions.StringClass

  def getType[T: ru.TypeTag](obj: T): ru.TypeTag[T] = ru.typeTag[T]

  def isExtend[T: ru.TypeTag, S: ru.TypeTag](t: T, s: S): Boolean = {
    val tTypeTag = ru.typeTag[T]
    val sTypeTag = ru.typeTag[S]

    tTypeTag.tpe <:< sTypeTag.tpe
  }

  case class User(age: Int, name: String)

  class E {
    type T
    val x: Option[T] = None
  }

  class A extends E

  class B extends E

}
