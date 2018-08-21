package ir.itstar.playground.utils

object AssertionSampleRunner extends App{

  println("scala assertion sample")

  def addNaturalNumbers(list: List[Int]): Int = {
    require(list forall (_ >= 0), "list contains negative numbers")
    val sum = list.sum
    assert(sum >= 0)
    sum
  }

  addNaturalNumbers(List(1,2,3,4))
  addNaturalNumbers(List(1,2,-3,4))

}
