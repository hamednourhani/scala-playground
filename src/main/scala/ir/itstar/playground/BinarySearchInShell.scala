package ir.itstar.playground

import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn._

object BinarySearchInShell extends App {

  run()

  def run() {
    print("what is the list size?\n")
    val n = readInt()
    print(" insert each item in one line : \n")
    var counter = 0
    val arrayBuffer = ArrayBuffer[Int]()
    while (counter < n) {
      val item = readInt()
      arrayBuffer.insert(counter, item.toInt)
      counter += 1
    }
    printf("what is the target?\n")
    val t = readInt()
    is(arrayBuffer.toArray, t)

    printf("do you want to continue?(type y|n)\n")
    val continue = readLine()
    if (continue == "y") run()
  }


  def is(list: Array[Int], target: Int): Unit = {

    if (list.length == 0) {
      println(false)
    } else if (list.length == 1) {
      if (list(0) == target) println(true)
      else println(false)
    } else {
      if (list.length % 2 == 1) {
        val currentIndex = list.length / 2
        val currentElem = list(currentIndex)
        if (currentElem == target) println(true)
        else {
          val (left, right) = list.splitAt(list.length / 2 + 1)
          val (_left, _) = list.splitAt(left.length - 1)
          if (currentElem < target) {
            is(right, target)
          } else {
            is(_left, target)
          }
        }
      } else {
        val currentIndex = list.length / 2 - 1
        val currentElem = list(currentIndex)
        if (currentElem == target) println(true)
        else {
          val (left, right) = list.splitAt(list.length / 2)
          val (_left, _) = list.splitAt(left.length - 1)
          if (currentElem < target) {
            is(right, target)
          } else {
            is(_left, target)
          }
        }
      }
    }
  }


}
