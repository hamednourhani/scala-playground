package ir.itstar.playground.base

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future

object BaseRunner extends App with LazyLogging{

  Option(Option(Option(2)))

  case class Info(data:Option[String])
  case class T(info:Option[Info])
  case class K(t:Option[T])


  val info = Info(Some("string"))
  val t = T(Some(info))
  val k = K(Some(t))


  val result: Option[String] = k.t.flatMap(_.info.flatMap(_.data))

  result match {
    case Some(a) =>
      logger.info(a.toString)
    case None =>
      logger.error("info is none")
  }

}

object FutureRunner extends App with LazyLogging{


  def func(s:String) ={
    logger.info(s)
    val s2 = s + "!"
    val result = new Fu{}

    val s3 = s2 + "3"
    s3

  }

  class Fu{
    val result : String = null
  }

  def func2(s:String) : Fu = ???







  func("hamed")


  case class T(
              n1:String,
              n2:String,
              n3:String,
              passengerInfo: PassengerInfo)

  case class PassengerInfo(n4:String,n5:String)

}
