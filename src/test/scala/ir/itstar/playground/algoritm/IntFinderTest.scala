package ir.itstar.playground.algoritm
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class IntFinderTest extends FlatSpec with Matchers{

  it should "return 7" in {
    val array = Array(1,10,3,4,5,6,8,9,2)
    val result =  IntFinder.solution(array)
    assert(result === 7)
  }


  it should "return 11" in {
    val array = Array(1,10,3,4,5,6,8,9,2,7)
    val result =  IntFinder.solution(array)
    assert(result === 11)
  }
}
