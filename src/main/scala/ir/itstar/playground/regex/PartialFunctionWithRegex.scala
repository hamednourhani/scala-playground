package ir.itstar.playground.regex
import scala.util.matching.Regex

trait RegexRule {
  val rule: Regex

  def apply(): PartialFunction[String, String] = {
    case rule(a, _*) => a
  }
}

object RegexRule {

  def createMatcher(l: Seq[RegexRule]): PartialFunction[String, String] =
    l.map(_.apply()).reduce(_.orElse(_))
}

object DateRegexRule extends RegexRule {
  val rule: Regex = "(\\d{4})-(\\d{2})-(\\d{2})".r
}


object NumberRegexRule extends RegexRule {
  val rule: Regex = "(\\d+),([A-z]+)".r
}

object AlphabeticRegexRule extends RegexRule {
  val rule: Regex = "([A-Z]+)".r
  override def apply(): PartialFunction[String, String] =
    super.apply().andThen(_.takeRight(4))

}

object PartialFunctionWithRegex extends App {

  val input = List("1,a", "23,zZ", "1991-12-31", "1ab", "", "ABCEDT")

  val regexRules: Seq[RegexRule] =
    Seq(DateRegexRule, NumberRegexRule, AlphabeticRegexRule)

  val matchers: PartialFunction[String, String] =
    RegexRule.createMatcher(regexRules)

  val result = input.collect(matchers)
  println(result)
  //List(1, 23, 1991, CEDT)

}
