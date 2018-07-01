package ir.itstar.playground

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._


object AkkaStreamRunner {

  println("akka stream example")

  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("akkaStreamActorSystem")

    implicit val materializer: ActorMaterializer = ActorMaterializer()

    def intToString(int: Int): String = s"$int"


    val graph = GraphDSL.create() {
      implicit builder =>

        val source = Source.fromIterator(() => (1 to 100000).iterator)
        val intToStringFlow: Flow[Int, String, NotUsed] = Flow.fromFunction(intToString)

        val A = builder.add(source).out
        val B = builder.add(intToStringFlow)
        val C = builder.add(Flow.fromFunction(println))
        val D = builder.add(Sink.ignore).in

        import GraphDSL.Implicits._

        A ~> B ~> C ~> D

        ClosedShape

    }

    val g = RunnableGraph.fromGraph(graph)

    g.run()
  }

}
