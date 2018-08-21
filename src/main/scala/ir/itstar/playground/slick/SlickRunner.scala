package ir.itstar.playground.slick

import ir.itstar.playground.db.repo.MessageRepo
import slick.jdbc.H2Profile.api._

object SlickRunner extends App with MessageRepo {



  override def db = Database.forConfig("h2db")

  val bathMessages = Seq(
    Message("content1", "sender1"),
    Message("content2", "sender2"),
    Message("content3", "sender3")
  )

  println("Creating database table")
  exec(run(messages.schema.create))


  println(messages.result.statements.mkString)


  println("Creating database table")
  await(run(messages.schema.create))

  println("Insert test data to message table")
  val initialInsert = messages ++= bathMessages
  println(initialInsert.statements.mkString(""))
  await(run(initialInsert))

  println(messages.result.statements.mkString)


  println("filter query")
  val content1 = messages.filter(_.sender === "sender1").result
  await(run(content1))

  await(run(messages.filter(_.sender === "sender1").result)).foreach {
    println
  }


}
