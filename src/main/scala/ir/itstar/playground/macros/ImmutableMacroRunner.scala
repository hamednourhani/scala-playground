package ir.itstar.playground.macros

import ir.itstar.tinyMacros.macros.Immutable

object ImmutableMacroRunner extends App {

  println("immutable macro runner")

//  implicit val isStringImmutable: Immutable[String] = Immutable.materialize[String]
//  implicit val isLongImmutable: Immutable[Long] = Immutable.materialize[Long]
//  implicit val isListImmutable: Immutable[List[_]] = Immutable.materialize[List[_]]

}
