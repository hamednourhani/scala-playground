package ir.itstar.playground.akka.stream

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, FlowShape}
import akka.stream.scaladsl.{Flow, GraphDSL, Partition, Sink, Source}
import com.typesafe.scalalogging.LazyLogging

object StreamPartition extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("StreamPartition")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val oddEvenFilter =
    GraphDSL.create() { implicit b =>
      // logic to dispatch odd and even numbers to different outlets
      b.add(Partition[Int](2, n => if (n % 2 == 0) 0 else 1))
    }


  // this flow uses the above filter
  val filterFlow = Flow.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._
    val input = b.add(oddEvenFilter)
    val sink1 = Sink.foreach[Int](n => println("even sink received " + n))
    input.out(0) ~> sink1
    FlowShape(input.in, input.out(1))
  })


  // connect to source and final sink
  Source(1 to 10)
    .via(filterFlow)
    .map(_ * 2)
    .runForeach(n => println("final sink received " + n))


}
