package ir.itstar.playground.akka

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.JsValue

object RestMock extends App {

  implicit val system:       ActorSystem       = ActorSystem("MockActorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

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
        entity(as[String]) { body => complete(StatusCodes.OK)
        }
      }
    }
  } ~ pathPrefix("test" / "logger" / "cancellation" / "consumer-event") {
    post {
      pathEndOrSingleSlash {
        entity(as[JsValue]) { body =>
          println(body.toString)
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
