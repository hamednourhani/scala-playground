package ir.itstar.playground.utils

import org.scalatest.{FlatSpec, Matchers}


class WriterTest extends FlatSpec with Matchers {


  it should "write hello world ! as log and calculate 24" in {


    val writes = for {
      a <- Writer(2).write("hello")
      b <- Writer(4).write("world")
      c <- Writer(3).write("!")
    } yield {
      a * b * c
    }
    val (result, logs) = writes.run
    (logs.mkString(" ") + result) shouldBe "hello world !24"

  }

  it should "write 1 2 3 * 2 sum" in {

    val writes = for {
      i <- Writer.empty[Int].write(4).writeIf(_ => true)(1)
      a <- Writer(2).write(1)
      b <- Writer(4).write(2)
      c <- Writer(3).write(3)
    } yield {
      a * b * c
    }
    val (result, logs) = writes.mapWritten(_ * 2).run
    logs.sum shouldBe 22

  }

}