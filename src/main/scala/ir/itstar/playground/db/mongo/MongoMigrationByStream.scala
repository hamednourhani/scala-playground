package ir.itstar.playground.db.mongo

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.alpakka.mongodb.scaladsl.{MongoSink, MongoSource}
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.{ActorAttributes, ActorMaterializer, Materializer, Supervision}
import org.bson.Document
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocumentWriter, Macros}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import reactivemongo.bson.{BSONDocumentReader, BSONDocumentWriter, Macros, document}


object MongoMigrationByStream {

  implicit val system: ActorSystem = ActorSystem("mongo-migration")
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  case class Person(name:String,modifiedDate:Long)

  val mongoUri = "mongodb://localhost:27017/mydb?authMode=scram-sha1"

  val driver = MongoDriver()
  val parsedUri: Try[MongoConnection.ParsedURI] = MongoConnection.parseURI(mongoUri)
  val connection: Try[MongoConnection] = parsedUri.map(driver.connection)

  def collection(connection: MongoConnection,dbName:String,colName:String): Future[BSONCollection] =
    connection.database(dbName).
      map(_.collection(colName))


  implicit def personWriter: BSONDocumentWriter[Person] = Macros.writer[Person]



  def changeModelAndInsertToNewCollection(person:Person) : Future[Boolean] ={
    //Todo : call mongo api to update the person
    ???
  }

  def processPeople()(implicit m: Materializer): Future[Done] = {
//    val numberOfConcurrentUpdate = 10
//
//    val peopleSource: Source[Person, Future[State]] =
//      collection
//        .find(json())
//        .cursor[Person]()
//        .documentSource()
//
//    peopleSource
//      .mapAsync(numberOfConcurrentUpdate)(changeModelAndInsertToNewCollection)
//      .withAttributes(ActorAttributes.supervisionStrategy(Supervision.restartingDecider))
//      .runWith(Sink.ignore)
    ???
  }

  //Todo : using alpakka as mongo db connector for akka stream
//  val source: Source[Document, NotUsed] =
//    MongoSource(collection.find(json()).cursor[Person]().documentSource())
//
//  source.runWith(MongoSink.updateOne(2, collection))



}
