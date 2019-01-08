package ir.itstar.playground.xml

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.xml.ScalaXmlSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.settings.{ClientConnectionSettings, ConnectionPoolSettings}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import scala.xml.{Elem, NodeSeq}

object SoapClient extends App {


  def sendRequest(): Unit = {

    val config = ConfigFactory.load()

    implicit val system: ActorSystem = ActorSystem("SoapClientActorSystem", config)
    implicit val ec: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()


    val apiBaseUrl = "################"

    val soapEntity =
      <soap-env:Envelope xmlns:mns="http://wbs.sepand4.com/soap/Psco4">
      <soap-env:Body>
        <mns:getPath>
          <wbsid xsi:type="xsd:string">###</wbsid>
          <user xsi:type="xsd:string">###########</user>
          <pass xsi:type="xsd:string">###########</pass>
        </mns:getPath>
      </soap-env:Body>
    </soap-env:Envelope>

    println(soapEntity)


    val connectionSettings = ClientConnectionSettings(system).withIdleTimeout(60.seconds)
    val connectionPoolSettings = ConnectionPoolSettings(system).withConnectionSettings(connectionSettings)

    val contentType: ContentType = ContentType(MediaTypes.`text/xml`, HttpCharsets.`UTF-8`)

    val result = for {
      requestEntity <- Marshal(soapEntity).to[RequestEntity]
      request = HttpRequest(HttpMethods.POST, apiBaseUrl)
        .withEntity(requestEntity.withContentType(contentType))
      //          .withHeaders(headers.RawHeader("SAOPAction","http://wbs.sepand4.com/site-v4/server.php/getPath"))
      response <- Http().singleRequest(request, settings = connectionPoolSettings)
      entity <- Unmarshal(response.entity).to[NodeSeq]
      status = response.status
    } yield {
      println(requestEntity)
      println(status)
      entity

    }

    result.onComplete {
      case Success(value) =>
        println(value)
      case Failure(e)     =>
        e.printStackTrace()
    }
  }


  def createContent(): Elem = {

    <mns:getpath xmlns:mns="" soap-env:encodingstyle="http://schemas.xmlsoap.org/soap/encoding/">
      <wbsId xsi:type="xsd:string">SNPP</wbsId>
      <user xsi:type="xsd:string">77899939906779</user>
      <pass xsi:type="xsd:string">Sn@PtrIp@5uiovpy%668</pass>
    </mns:getpath>

  }


  sendRequest()
}


object T {
  case class User(name:String,family:String){
    def toxml = {
      <name xsi:type="xsd:string">{name}</name>
        <family xsi:type="xsd:string">{name}</family>

    }
  }
}
