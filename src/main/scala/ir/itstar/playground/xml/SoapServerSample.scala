package ir.itstar.playground.xml

import javax.jws.WebService
import javax.xml.ws.Endpoint

import akka.actor.ActorSystem

@WebService(targetNamespace="ir.itstar.test", name="ir.itstar.test", portName="test", serviceName="WSTest")
class SoapServerSample(system: ActorSystem) {

  def test(value : String): String = {
    s"Hello $value"
  }

}

object MySoapServer extends App {
  // Create an Akka system
  implicit val system: ActorSystem = ActorSystem("application")

  // Create endpoint
  val endpoint = Endpoint.publish("http://localhost:8080/wstest", new SoapServerSample(system))
  println("Waiting for requests...")
}