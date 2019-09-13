package ir.itstar.playground.algoritm
import scala.collection.mutable

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class SampleSpec extends FlatSpec with Matchers {

  it should "return 3" in {
    val a: Array[Int] = Array(2, 3, 1, 5, 4)
    assert(SampleSpec.solutionA(a) === 2)
  }

  it should "return " in {
    val a: Array[Int] = Array(1, 3, 4, 2, 5)
    assert(SampleSpec.solutionA(a) === 3)
  }

  it should "return 7" in {

    val s = "011100"

    assert(SampleSpec.solutionB(s) === 7)

  }

  it should "return 8" in {

    val s = "011110"
    assert(SampleSpec.solutionB(s) === 8)

  }

  it should "return 7 by utils" in {

      val b = Utils.toBinary(28)
    println(b)
    val s = s"$b"
    println(s)

    assert(SampleSpec.solutionB(s) === 7)

  }


  it should "return 8 by utils" in {

    val b = Utils.toBinary(29)
    println(b)
    val s = s"$b"
    println(s)


    assert(SampleSpec.solutionB(s) === 8)

  }


  it should "return 18 by utils" in {

    val b = Utils.toBinary(279)
    println(b)
    val s = s"$b"
    println(s)


    assert(SampleSpec.solutionB(s) === 13)

  }

  it should "return 4" in {
    //[3, 5, 6, 3, 3, 5]
    val a = Array(3, 5, 6, 3, 3, 5)

    assert(SampleSpec.solutionC(a) === 4)
  }

}

object SampleSpec {

  def solutionA(a: Array[Int]): Int = {
    val hashMap: mutable.HashMap[Int, Unit] = mutable.HashMap()

    var resultCount = 0

    a.foreach { i =>
      hashMap += (i -> ())
      if ((1 to i).forall(j => hashMap.contains(j)))
        resultCount += 1
    }
    resultCount
  }

  def solutionB(s: String): Int = {
    val firstOne = s.indexOf('1')
    val subst    = s.substring(firstOne, s.length)
    val len      = subst.length
    println(len)
    val numberOfOne = subst.count(_ == '1')
    println(numberOfOne)
    len + numberOfOne - 1
  }

  def solutionC(a: Array[Int]): Int = {
    import scala.collection.mutable
    val m = mutable.HashMap[Int, Int]()
    a.foreach { i =>
      m.get(i).fold {
        m += (i -> 1)
      } { c =>
        m += (i -> (c + 1))
      }
    }

    m.foldLeft(0) { (acc, y) =>
      if (acc < 1000000000)
        acc + (y._2 * (y._2 - 1)) / 2
      else
        1000000000
    }
  }

}

object Utils {

  def toBinary(n: Int, bin: List[Int] = List.empty[Int]): String =
    if (n / 2 == 1) (1 :: (n % 2) :: bin).mkString("")
    else {
      val r = n % 2
      val q = n / 2
      toBinary(q, r :: bin)
    }
}
