package ir.itstar.playground.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

import scala.collection.mutable
import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}

object StreamIdenticalElements extends App {

  implicit val system: ActorSystem = ActorSystem("StreamRunner")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher


  //Test with Integer list
  //  val list = List(1, 2, 3, 4, 3, 5)
  //
  //  val set = mutable.Set.empty[Int]
  //
  //  val f = Source
  //    .fromIterator(list.iterator _)
  //    .map { i =>
  //      val res = !set.contains(i)
  //      set += i
  //      res
  //    }.takeWhile(identity,inclusive = true)
  //    .runWith(Sink.last)
  //
  //  f.onComplete {
  //    case Success(value)=>
  //      println(value)
  //
  //    case Failure(e)=>
  //      e.printStackTrace()
  //  }


  //Test with mock data

  case class Color(n: String, name: String, lightOrDark: String, n2: String)

  val k1 = Color("1", "red", "light", "10")
  val k2 = Color("1", "blue", "dark", "11")
  val k3 = Color("1", "orange", "dark", "11")
  val k4 = Color("1", "red", "light", "10")
  val k5 = Color("1", "red", "dark", "200")

  println(k1.hashCode() == k2.hashCode())
  println(k1.hashCode() == k4.hashCode())

  val set = mutable.Set.empty[Int]

  val colorList = List(k1, k2, k3, k4, k5)

  val restResult = Source.fromIterator(colorList.iterator _)

    .map { color =>
      val hashCode = color.hashCode()
      val res = !set.contains(hashCode)
      set += hashCode
      res
    }.takeWhile(identity, inclusive = true)
    .runWith(Sink.last)


  restResult.onComplete {
    case Success(value) =>
      println(value)
      system.terminate()

    case Failure(e) =>
      e.printStackTrace()
      system.terminate()
  }


}
