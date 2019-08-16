package ir.itstar.playground.algoritm

object FindSameDigitNumber {

  private val digitsArray =
    Array(0, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000)

  def solution(n: Int): Int = {
    val numberOfDigits = s"$n".length - 1
    digitsArray(numberOfDigits)
  }
}
