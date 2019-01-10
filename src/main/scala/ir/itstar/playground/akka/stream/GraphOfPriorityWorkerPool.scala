package ir.itstar.playground.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.FanInShape.{Init, Name}
import akka.stream._
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge, MergePreferred, RunnableGraph, Sink, Source}

import scala.collection.immutable
object GraphOfPriorityWorkerPool {

  implicit val system: ActorSystem = ActorSystem("StreamStarter")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  // A shape represents the input and output ports of a reusable
  // processing module
  case class PriorityWorkerPoolShape[In, Out](
                                               jobsIn:         Inlet[In],
                                               priorityJobsIn: Inlet[In],
                                               resultsOut:     Outlet[Out]) extends Shape {

    // It is important to provide the list of all input and output
    // ports with a stable order. Duplicates are not allowed.
    override val inlets: immutable.Seq[Inlet[_]] =
    jobsIn :: priorityJobsIn :: Nil
    override val outlets: immutable.Seq[Outlet[_]] =
      resultsOut :: Nil

    // A Shape must be able to create a copy of itself. Basically
    // it means a new instance with copies of the ports
    override def deepCopy() = PriorityWorkerPoolShape(
      jobsIn.carbonCopy(),
      priorityJobsIn.carbonCopy(),
      resultsOut.carbonCopy())

  }

  class PriorityWorkerPoolShape2[In, Out](_init: Init[Out] = Name("PriorityWorkerPool"))
    extends FanInShape[Out](_init) {
    protected override def construct(i: Init[Out]) = new PriorityWorkerPoolShape2(i)

    val jobsIn: Inlet[In] = newInlet[In]("jobsIn")
    val priorityJobsIn: Inlet[In] = newInlet[In]("priorityJobsIn")
    // Outlet[Out] with name "out" is automatically created
  }

  object PriorityWorkerPool {
    def apply[In, Out](
                        worker:      Flow[In, Out, Any],
                        workerCount: Int): Graph[PriorityWorkerPoolShape[In, Out], NotUsed] = {

      GraphDSL.create() { implicit b ⇒
        import GraphDSL.Implicits._

        val priorityMerge = b.add(MergePreferred[In](1))
        val balance = b.add(Balance[In](workerCount))
        val resultsMerge = b.add(Merge[Out](workerCount))

        // After merging priority and ordinary jobs, we feed them to the balancer
        priorityMerge ~> balance

        // Wire up each of the outputs of the balancer to a worker flow
        // then merge them back
        for (i ← 0 until workerCount)
          balance.out(i) ~> worker ~> resultsMerge.in(i)

        // We now expose the input ports of the priorityMerge and the output
        // of the resultsMerge as our PriorityWorkerPool ports
        // -- all neatly wrapped in our domain specific Shape
        PriorityWorkerPoolShape(
          jobsIn = priorityMerge.in(0),
          priorityJobsIn = priorityMerge.preferred,
          resultsOut = resultsMerge.out)
      }

    }


    val worker1: Flow[String, String, NotUsed] = Flow[String].map("step 1 " + _)
    val worker2: Flow[String, String, NotUsed] = Flow[String].map("step 2 " + _)

    RunnableGraph.fromGraph(GraphDSL.create() { implicit b ⇒
      import GraphDSL.Implicits._

      val priorityPool1 = b.add(PriorityWorkerPool(worker1, 4))
      val priorityPool2 = b.add(PriorityWorkerPool(worker2, 2))

      Source(1 to 100).map("job: " + _) ~> priorityPool1.jobsIn
      Source(1 to 100).map("priority job: " + _) ~> priorityPool1.priorityJobsIn

      priorityPool1.resultsOut ~> priorityPool2.jobsIn
      Source(1 to 100).map("one-step, priority " + _) ~> priorityPool2.priorityJobsIn

      priorityPool2.resultsOut ~> Sink.foreach(println)
      ClosedShape
    }).run()

  }

}
