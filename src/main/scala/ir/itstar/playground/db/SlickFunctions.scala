package ir.itstar.playground.db

import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.{Await, Future}
import concurrent.duration._

trait SlickFunctions {

  def db: H2Profile.backend.Database

  def run[T](action: DBIO[T]): Future[T] = db.run(action)

  def exec[T](f:Future[T]) : T = Await.result(f,Duration.Inf)

  def await[T](f:Future[T]) : T = Await.result(f,Duration.Inf)

}

