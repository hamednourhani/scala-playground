package ir.itstar.playground.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, KillSwitches, UniqueKillSwitch}
import akka.stream.scaladsl.{Keep, Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import ir.itstar.playground.akka.RestMock.routes

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object HandleWebSocketDisconnection extends App with LazyLogging{

  implicit val system: ActorSystem = ActorSystem("HandleWebSocketDisconnection")
  implicit val ec :ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()



  private val wsReader: Route =
    path("v1" / "data" / "ws") {
      logger.info("Opening websocket connecting ...")

      val sharedKillSwitch = KillSwitches.shared("my-kill-switch")

      val testSource =
        Source
        .repeat("Hello")
        .throttle(1, 1.seconds)
        .map(x => {
        println(x)
        x
      })
      .map(TextMessage.Strict)
        .limit(1000)
          .via(sharedKillSwitch.flow)

      extractUpgradeToWebSocket { upgrade ⇒
        val inSink =
          Sink.onComplete(_ => sharedKillSwitch.shutdown())
        val outSource = testSource
        val socket =
          upgrade
            .handleMessagesWithSinkSource(inSink, outSource)

        complete(socket)
      }
    }

  val f = Http().bindAndHandle(wsReader, "localhost", 9090)

  f.flatMap(_.unbind())
}


object HandleWebSocketDisconnectionTest extends App with LazyLogging{

  implicit val system: ActorSystem = ActorSystem("HandleWebSocketDisconnection")
  implicit val ec :ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()



      logger.info("Opening websocket connecting ...")

      val sharedKillSwitch = KillSwitches.shared("my-kill-switch")

      val testSource =
        Source
          .repeat("Hello")
          .throttle(1, 1.seconds)
          .map(TextMessage.Strict)
          .limit(1000)
          .via(sharedKillSwitch.flow)
            .runWith(Sink.foreach(i => logger.info(i.text)))


      Source
        .single("kill source 1")
        .delay(5.second)
        .runWith(Sink.foreach(i => logger.info(i)))
        .onComplete{_ =>
          sharedKillSwitch.shutdown()
          system.terminate()
        }

}
