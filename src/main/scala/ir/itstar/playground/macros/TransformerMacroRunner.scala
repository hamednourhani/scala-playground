package ir.itstar.playground.macros

import scala.concurrent.Future

//import ir.itstar.tinyMacros.macros.{Transformer, TransformerMacro}

object TransformerMacroRunner extends App {

  println("transformer macro runner")



//  implicit val initTransformer: Transformer[Int] = TransformerMacro.transform[Int]
//  implicit val stringTransformer: Transformer[String] = TransformerMacro.transform[String]
//  implicit val longTransformer: Transformer[Long] = TransformerMacro.transform[Long]
//
//  def toOption[T](data: T)(implicit transformer: Transformer[T]): Option[T] = {
//    transformer.toOption(data)
//  }

//  println(toOption(100))
//  println(toOption("String"))
//  println(toOption(100000L))


}

object t extends App {


  val k: List[Int] = 2 :: (3 :: Nil)

  println(k)


  implicit class StringOps(val s:String) extends AnyVal {
    def len: Int = s.length
  }

  def func(i:Int) = println(s"$i")

  implicit def stringToInt(s:String) = s.length

  func("hamed")


  class T(){
    def func(s:String): Int ={
      func2(s.length)
    }

    def func2(i:Int) = i * 2
  }



  "hamed".len


//  Future.successful(2).flatMap(i => )

  case class Person(name:String,age:Int)


  trait Validation[T]{
    def validate(t:T): Either[Throwable,T]
  }


  object Validation{
    def apply[T](f:T => Boolean): Validation[T] = new Validation[T]{
      override def validate(t: T): Either[Throwable, T] =
        if(f(t)) Right(t)
        else Left(new Exception(s"$t is not valid"))
    }
  }

  implicit class ValidatorOps[T](t:T){
    def validate(implicit v:Validation[T]): Either[Throwable,T] = v.validate(t)
  }

  implicit val personValidator: Validation[Person] = Validation[Person](_.age > 34)

  Person("hamed",23).validate




}
