package ir.itstar.playground.utils

import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.Future


class TestAsyncTest extends AsyncFlatSpec with AsyncMockFactory with Matchers {

  val underly: ChildTrait = stub[ChildTrait]

  val parent = new Parent(underly)

  (underly.foo _).when(*).returns(_)
  (underly.bar _).when(*).returns(_)

  it should "be true" in {
    for {
      c <- Future("C")
      _ <- parent.foo("a")
      _ <- parent.bar("b")
      _ = (underly.foo _).verify("a")
      _ = (underly.bar _).verify("b")
    } yield {
      println(c)
      succeed

    }

  }

}

trait ChildTrait {
  def foo(t: String): String

  def bar(d: String): String
}


class Parent(child: ChildTrait) {

  import scala.concurrent.ExecutionContext.Implicits.global

  def foo(t: String): Future[String] = Future(child.foo(t))

  def bar(t: String): Future[String] = Future(child.bar(t))
}
