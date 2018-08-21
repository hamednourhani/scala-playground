package ir.itstar.playground

import org.mongodb.scala.bson.{BsonArray, BsonDocument, BsonString, BsonTransformer}

import scala.io.Source

object JsonToBsonRunner extends App {

  println("BsonDocument transformer sample")

  case class TestClass(id: String, name: String)

  val bson = BsonDocument("id" -> TestClass("id", "name"))

  implicit val transform: BsonTransformer[TestClass] = (value: TestClass) => {
    BsonArray(
      List("id" -> BsonString(value.id), "name" -> BsonString(value.name))
    )
  }

  println("json to bson runner")

  val json = Source.fromResource("profile.json").mkString("")
  val bsonFromJsonString = BsonDocument(json)

  println(bson)

}
