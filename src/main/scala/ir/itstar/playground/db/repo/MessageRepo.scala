package ir.itstar.playground.db.repo

import ir.itstar.playground.db.SlickFunctions
import slick.jdbc.H2Profile.api._

trait MessageRepo extends SlickFunctions {

  final case class Message(content: String, sender: String, id: Long = 0L)

  final class MessageTable(tag: Tag) extends Table[Message](tag, "Messages") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def content = column[String]("CONTENT")

    def sender = column[String]("SENDER")

    def * = (content, sender, id).mapTo[Message]
  }

  val messages = TableQuery[MessageTable]

}
