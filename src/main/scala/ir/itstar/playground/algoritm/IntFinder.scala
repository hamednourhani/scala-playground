package ir.itstar.playground.algoritm

import scala.collection.JavaConverters._

// you can write to stdout for debugging purposes, e.g.
// println("this is a debug message")

object IntFinder {
  def solution(a: Array[Int]): Int = {
    val max = 1000000
    val mapped = a.filter(_ > 0).map(i => i -> i).toMap
      (1 to max).toStream.find(i => mapped.get(i).isEmpty) match {
        case Some(res) => res
        case None => max + 1
      }

  }
}

