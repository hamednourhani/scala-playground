package ir.itstar.playground.macros

import ir.itstar.tinyMacros.macros.Query._
import ir.itstar.tinyMacros.macros.{Query, Table}

object QueryMacrosRunner extends App{

  println("query macros runner")

  case class User(name: String)

  val user = User("name")

  val userTable: Table[User] = Table[User]()

//  val result: Query[String] = userTable.map(_.name)
//
//  println(result)

}
