package ir.itstar.playground.utils

import akka.actor.{Actor, ActorKilledException, ActorSystem, Kill, Props}
import akka.testkit.EventFilter
import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

object TestActorTest extends FlatSpec with Matchers {

  println("akka stream example")


  val config = ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener]""")

  implicit val system = ActorSystem("testActorSystem", config)

  class T extends Actor {
    override def receive: Receive = {
      case _ => Unit
    }
  }

  try {
    val actor = system.actorOf(Props[T])

    EventFilter[ActorKilledException](occurrences = 1) intercept {
      actor ! Kill
    }
  }

}
