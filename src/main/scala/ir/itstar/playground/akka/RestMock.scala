package ir.itstar.playground.akka

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import ir.itstar.playground.akka.RestMock.LogIt

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object RestMock extends App {

  implicit val system: ActorSystem = ActorSystem("MockActorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  lazy val loggerActor = system.actorOf(Props(new LoggerActor()), "LoggerActor")

  sys.addShutdownHook(system.terminate())

  val snappContactInfo: String =
    """
      |{
      | "status" : 123,
      | "data" : {
      |     "name" : "hamed",
      |     "email" : "email@example.com",
      |     "phone" : "23156454",
      |     "cellphone" : "091215454"
      |    }
      |}
    """.stripMargin

  val routes =
    post {
      path("logger") {
        pathEndOrSingleSlash {
          entity(as[String]) { body =>
            loggerActor ! LogIt(body)
            complete(StatusCodes.OK)
          }
        }
      }
    }

  val server = Http().bindAndHandle(routes, "localhost", 9090)
  println("server started at : localhost:9090")
  StdIn.readLine()

  server.flatMap(_.unbind())

  case class LogIt(body: String)

}


class LoggerActor extends Actor with ActorLogging {
  import spray.json._
  import DefaultJsonProtocol._ // if you don't supply your own Protocol (see below)

  override def receive: Receive = {
    case LogIt(body: String) =>
      log.debug(body)
      log.debug(body.parseJson.prettyPrint)
  }
}
