package ir.itstar.playground.akka.stream

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorMaterializer, Attributes}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Random, Success}
import scala.concurrent.duration._


/**
  *  A sample runner to show how to log messages between stream stages
  */
object StreamLogging extends App {

  implicit val system: ActorSystem = ActorSystem("StreamLoggingActorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val adapter: LoggingAdapter = Logging(system, "customLogger")
  implicit val ec: ExecutionContextExecutor = system.dispatcher

  def randomInt = Random.nextInt()

  val source = Source.repeat(NotUsed).map(_ ⇒ randomInt)


  val logger = source
    .groupedWithin(Integer.MAX_VALUE, 5.seconds)
    .log(s"in the last 5 seconds number of messages received : ", _.size)
    .withAttributes(
      Attributes.logLevels(
        onElement = Logging.WarningLevel,
        onFinish = Logging.InfoLevel,
        onFailure = Logging.DebugLevel
      )
    )

  val sink = Sink.ignore

  val result: Future[Done] = logger.runWith(sink)

  result.onComplete{
    case Success(_) =>
      println("end of stream")
    case Failure(_) =>
      println("stream ended with failure")
  }

}
