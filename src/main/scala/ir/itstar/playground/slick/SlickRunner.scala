package ir.itstar.playground.slick

import ir.itstar.playground.db.repo.MessageRepo
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

object SlickRunner extends App with MessageRepo {
  override def db = Database.forConfig("h2db")

  val bathMessages = Seq(
    Message("content1", "sender1"),
    Message("content2", "sender2"),
    Message("content3", "sender3")
  )

  println("Creating database table")
  exec(run(messages.schema.create))

  val initialInsert = messages ++= bathMessages

  println(messages.result.statements.mkString)

  val allMessages = initialInsert andThen messages.result

  val all: Future[Seq[Message]] = run(allMessages)

  println(exec(all))

}
