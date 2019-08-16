package ir.itstar.playground.algoritm
import org.scalatest.FlatSpec
import org.scalatest.Matchers

class FindSameDigitNumberTest extends FlatSpec with Matchers{

  it should "return 0" in {
    val result = FindSameDigitNumber.solution(1)
    assert(result === 0)
  }

  it should "return 100000" in {
    val result = FindSameDigitNumber.solution(928978)
    assert(result === 100000)
  }


  it should "return 10" in {
    val result = FindSameDigitNumber.solution(10)
    assert(result === 10)
  }


  it should "return 100" in {
    val result = FindSameDigitNumber.solution(125)
    assert(result === 100)
  }

}
