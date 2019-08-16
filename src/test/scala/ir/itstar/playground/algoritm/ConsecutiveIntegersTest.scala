package ir.itstar.playground.algoritm
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class ConsecutiveIntegersTest extends FlatSpec with Matchers{

  it should "return 3" in {
    val result = ConsecutiveIntegers.solution(6,20)
    assert(result === 3)
  }


  it should "return 0" in {
    val result = ConsecutiveIntegers.solution(21,29)
    assert(result === 0)
  }

  it should "return 1" in {
    val result = ConsecutiveIntegers.solution(30,35)
    assert(result === 1)
  }

  it should "return 306" in {
    val result = ConsecutiveIntegers.solution(100,100000)
    assert(result === 306)
  }

}
