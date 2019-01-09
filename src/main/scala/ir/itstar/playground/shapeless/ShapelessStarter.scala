package ir.itstar.playground.shapeless

import com.typesafe.scalalogging.LazyLogging
import shapeless._
import cats._
import shapeless.ops.hlist._
import shapeless.syntax.std.tuple._
import shapeless.HList

import scala.util.Try


object ShapelessStarter extends App with LazyLogging{

  case class Model(st:String,id:Long)

  //gen
  val objects = Model("St",1l) :: true :: false :: HNil

  //get
  val model: Model = objects.select[Model]

  val list: Boolean = objects.select[Boolean]

  val head: Model = objects.head

  //polymorphic function to use with higher order functions
  object plusOne extends Poly1{
    implicit def caseInt = at[Int]{_ + 1}

    implicit def caseString = at[String]{_ + 1}

    implicit def caseUser = at[Model]{_.id + 1}
  }

  //map
  objects.map(plusOne)


  implicit val stringShow: Show[String] = Show.fromToString[String]
  implicit val intShow: Show[Boolean] = Show.fromToString[Boolean]
  implicit val modelShow: Show[Model] = Show.fromToString[Model]

  object show extends Poly1{
    implicit def show[A](implicit s:Show[A]) =
      at[A]{a => "showing" + s.show(a)}
  }

//  objects.map(show)

  //Generics
  val gen = Generic[Model]
  val h = gen.to(model) //St :: 1l :: HNil
  gen.from(h) // Model(St,1l)


  //tuples
  (1, "foo", 12.3).tail // returns (foo,12.3)

  //lens
  val nameLens = lens[Model].name

  val m = Model("hamed",2)
  //    nameLens.get(m)
  //  val m2 = nameLens.set(m)("hamid")


  //Arity
  class MyClass[H <: HList](hs: H)

  object MyClass {
        def apply[P <: Product, L <: HList](p: P)(implicit gen: Generic.Aux[P, L]) =
      new MyClass[L](gen.to(p))
    }

  val k: MyClass[Int :: String :: HNil] = MyClass(1,"Hello")
  MyClass(1,"Hello","world")
  MyClass(1,"Hello","world","!")
  MyClass(1,"Hello","world","!",true)


  val int = 3

  val int2 = 0

  val k: Int = Try(int/int2).toOption.getOrElse(0)


  trait Monad[+A]{
    def pure[A](a :A) : Monad[A]
    def map[B](f:A => B) : Monad[B]
    def flatMap[B](f : A => Monad[B]) : Monad[B]
  }


  val i = 2

  Option(i).map(_.toString).flatMap(_ => Option(true))








}
