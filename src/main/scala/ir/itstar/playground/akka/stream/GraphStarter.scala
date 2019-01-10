package ir.itstar.playground.akka.stream

import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Keep, Merge, RunnableGraph, Sink, Source, Zip, ZipWith}
import akka.stream._
import com.typesafe.scalalogging.LazyLogging

import scala.collection.immutable
import scala.concurrent.{Await, Future}


object GraphStarter extends App with LazyLogging {

  implicit val system: ActorSystem = ActorSystem("StreamStarter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  /**
    * Create CloseShape Graph
    */

  val closedShapeGraph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder =>

    import GraphDSL.Implicits._

    val in = Source(1 to 10)
    val out = Sink.foreach(println)

    val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

    val broadcast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))

    in ~> f1 ~> broadcast ~> f2 ~> merge ~> f3 ~> out
    broadcast ~> f4 ~> merge

    ClosedShape
  })

  // closedShapeGraph.run()


  val topHead = Sink.head[Int]
  val bottomHead = Sink.head[Int]
  val doubler = Flow[Int].map(_ * 2)

  val graph = RunnableGraph.fromGraph(GraphDSL.create(topHead,bottomHead)((_,_)){ implicit builder =>
    (tH,bH) =>
    import GraphDSL.Implicits._

    val bCast = builder.add(Broadcast[Int](2))
      Source.single(1) ~> bCast.in

      bCast ~> doubler ~> tH.in
      bCast ~> doubler ~> bH.in

    ClosedShape

  })




  //
  val sinks = immutable.Seq("a", "b", "c").map(prefix ⇒
    Flow[String].filter(str ⇒ str.startsWith(prefix)).toMat(Sink.head[String])(Keep.right)
  )


  val g: RunnableGraph[Seq[Future[String]]] = RunnableGraph.fromGraph(GraphDSL.create(sinks) { implicit b ⇒ sinkList ⇒
    import GraphDSL.Implicits._
    val broadcast = b.add(Broadcast[String](sinkList.size))

    Source(List("ax", "bx", "cx")) ~> broadcast
    sinkList.foreach(sink ⇒ broadcast ~> sink)

    ClosedShape
  })

  //val matList: Seq[Future[String]] = g.run()


  /**
    * partial graphs
    */

  val pickMaxOfThree = GraphDSL.create() { implicit b ⇒
    import GraphDSL.Implicits._

    val zip1 = b.add(ZipWith[Int, Int, Int](math.max _))
    val zip2 = b.add(ZipWith[Int, Int, Int](math.max _))
    zip1.out ~> zip2.in0

    UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip2.in1)
  }

  val resultSink = Sink.head[Int]

  val partialGraph = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit b ⇒ sink ⇒
    import GraphDSL.Implicits._

    // importing the partial graph will return its shape (inlets & outlets)
    val pm3 = b.add(pickMaxOfThree)

    Source.single(1) ~> pm3.in(0)
    Source.single(2) ~> pm3.in(1)
    Source.single(3) ~> pm3.in(2)
    pm3.out ~> sink.in
    ClosedShape
  })

  //val max: Future[Int] = partialGraph.run()



  /**
    * SourceShape from partial graph
    */
  val pairs = Source.fromGraph(GraphDSL.create() { implicit b ⇒
    import GraphDSL.Implicits._

    // prepare graph elements
    val zip = b.add(Zip[Int, Int]())
    def ints = Source.fromIterator(() ⇒ Iterator.from(1))

    // connect the graph
    ints.filter(_ % 2 != 0) ~> zip.in0
    ints.filter(_ % 2 == 0) ~> zip.in1

    // expose port
    SourceShape(zip.out)
  })

  val firstPair: Future[(Int, Int)] = pairs.runWith(Sink.head)


  /**
    * FlowShape from partial graph
    */

  val pairUpWithToStringFlow: Flow[Int, (Int, String), NotUsed] =
    Flow.fromGraph(GraphDSL.create() { implicit b ⇒
      import GraphDSL.Implicits._

      // prepare graph elements
      val broadcast = b.add(Broadcast[Int](2))
      val zip = b.add(Zip[Int, String]())

      // connect the graph
      broadcast.out(0).map(identity) ~> zip.in0
      broadcast.out(1).map(_.toString) ~> zip.in1

      // expose ports
      FlowShape(broadcast.in, zip.out)
    })

  pairUpWithToStringFlow.runWith(Source(List(1)), Sink.head)


  /**
    * Combining Sources and Sinks with simplified API
    */

    //Source from combined sources
  val sourceOne = Source(List(1))
  val sourceTwo = Source(List(2))
  val merged: Source[Int, NotUsed] = Source.combine(sourceOne, sourceTwo)(Merge(_))

  val mergedResult: Future[Int] = merged.runWith(Sink.fold(0)(_ + _))


  //Sink from combined sinks
  val actorRef : ActorRef = ???
  val sendRemotely = Sink.actorRef(actorRef, "Done")
  val localProcessing = Sink.foreach[Int](_ ⇒ /* do something useful */ ())

  val sink = Sink.combine(sendRemotely, localProcessing)(Broadcast[Int](_))

  Source(List(0, 1, 2)).runWith(sink)


  /**
    * get ride of stream cycle by OverflowStartegy.dropHead
    */
  val sor = Source(1 to 100)
  RunnableGraph.fromGraph(GraphDSL.create(sor) { implicit b =>
    source =>
    import GraphDSL.Implicits._

    val merge = b.add(Merge[Int](2))
    val bcast = b.add(Broadcast[Int](2))

    source ~> merge ~> Flow[Int].map { s => println(s); s } ~> bcast ~> Sink.ignore
    merge <~ Flow[Int].buffer(10, OverflowStrategy.dropHead) <~ bcast
    ClosedShape
  })



}
