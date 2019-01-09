package ir.itstar.playground.cats

import cats.{Applicative, Functor}
import com.typesafe.scalalogging.LazyLogging
import spray.json.{JsNumber, JsString, JsValue}
import cats.instances.option._
import cats.instances.list._
import cats.instances.future._
import cats.Show._
import cats.syntax.option._
import cats.Applicative._
import cats.Functor._
import cats.instances.tuple._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global




object CatsStarter extends App with LazyLogging{

  //Type class
  trait Codec[A]{
    def read(j:JsValue) : A
    def write(a:A) : JsValue

  }


  //Instance of Sample
  class StringSample extends Codec[String]{

    override def write(a :String) : JsValue = JsString(a)

    override def read(j: JsValue): String = {
      j match {
        case JsString(a) => a
        case _ => throw  new Exception("")
      }
    }
  }


  class IntSample extends Codec[Int]{

    override def write(a: Int): JsValue = JsNumber(a)

    override def read(j: JsValue): Int = {
      j match {
        case JsNumber(a) => a.toInt
        case _ => throw  new Exception("")
      }
    }
  }


  implicit val sFormater = new StringSample
  implicit val iFormater = new IntSample



  def toJson[A](a:A)(implicit codec: Codec[A]) : JsValue ={
    codec.write(a)
  }


  implicit class StringSyntax(s:String) {
    def toJson(implicit codec: Codec[String]): JsValue = codec.write(s)
    def len  = s.length
  }

  "hamed".toJson


  toJson("s")
  toJson(2)

  val k: JsValue = "hamed".toJson

  "hamid".len


  val o1 = Some(2)
  val o2 = None


  o1.flatMap(i => o2.map(j => i+j))

  val optionApplicative = Applicative[Option[Int]]

  val optionListApplicative = Applicative[Option].compose[List]

  val p: Option[(Int, Int)] = optionApplicative.product(o1,o2)

  val m: Option[Int] = optionApplicative.map2(o1,o2)(_ + _)

  val l1 = Some(List(1))

  val l2 = Some(List(3))

  val l3 = Some(List(4))


  val l4: Option[List[Int]] = optionListApplicative.map2(l1,l2)(_ + _)


  val f1 = Future.successful(Some(1))
  val f2 = Future.successful(Some(4))
  val futureOptionApplicative = Applicative[Future].compose[Option]
  val f3: Future[Option[Int]] = futureOptionApplicative.map2(f1,f2)(_ + _)
  futureOptionApplicative.map3(Future(Some(1)),Future(None),Future(Some(3)))(_ * _ + _)

  val futureOptionListApplicative = futureOptionApplicative.compose[List]
  val f4 = Future.successful(Some(List.empty[Int]))
  val f5 = Future.successful(Some(List(1)))
  futureOptionListApplicative.map2[Int,Int,Int](f4,f5)(_ + _)

  val futureOptionFunctor = Functor[Future].compose[Option]

  Future(Some(2)).map(_.map(_ * 2))

  futureOptionFunctor.map(Future(Some(2)))(_ * 2)

  val s: Future[Option[(String, Int)]] = futureOptionFunctor.tupleLeft(Future(Some(2)),"Hamed")
  val s2: Future[Option[(Int, String)]] = futureOptionFunctor.tupleRight(Future(Some(2)),"Hamed")
}
