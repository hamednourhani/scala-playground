package ir.itstar.playground.macros

import scala.language.experimental.macros

/**
  * run it with flags to observe program AST in compile time:
  *
  * scalac -Xprint:parser -Xprint:typer -uniqid -Yshow-trees-compact -Yshow-trees-stringified
  *
  */
object DebugOnScalaCompileTime {
  val xs = List(42)
  val head: Int = xs.head

  println((xs, head))

  //  PackageDef(Select(Select(Select(Ident("ir#28"
  //  ), "ir.itstar#6566"
  //  ), "ir.itstar.playground#6568"
  //  ), "ir.itstar.playground.macros#6570"
  //  ), List(ModuleDef(Modifiers(), "ir.itstar.playground.macros.DebugOnScalaCompileTime#6576"
  //  , Template(List(Select(Ident("scala#22"
  //  ), TypeName("AnyRef")#1924")), noSelfType
  //  , List(DefDef(Modifiers(), "termNames.CONSTRUCTOR#6664"
  //  , List()
  //  , List(List())
  //  , TypeTree()
  //  , Block(List(Apply(Select(Super(This(TypeName("DebugOnScalaCompileTime")), typeNames.EMPTY), "termNames.CONSTRUCTOR#2259"
  //  ), List()
  //  ) ), Literal(Constant(()))
  //  ) ), ValDef(Modifiers(PRIVATE | LOCAL), TermName("xs")#6665
  //  , TypeTree()
  //  , Apply(TypeApply(Select(Select(Select(Select(Ident("scala#22"
  //  ), "scala.collection#1912"
  //  ), "scala.collection.immutable#4221"
  //  ), "scala.collection.immutable.List#7487"
  //  ), TermName("apply")"#7691"
  //  ), List(TypeTree())
  //  ), List(Literal(Constant(42)))
  //  ) ), DefDef(Modifiers(METHOD | STABLE | ACCESSOR), TermName("xs")"#6666"
  //  , List()
  //  , List()
  //  , TypeTree()
  //  , Select(This(TypeName("DebugOnScalaCompileTime")), TermName("xs ")"#6665"
  //  ) ), ValDef(Modifiers(PRIVATE | LOCAL), TermName("head ")"#6667"
  //  , TypeTree().setOriginal(Select(Ident("scala#22"
  //  ), "scala.Int#923"
  //  ) ), Select(Select(This(TypeName("DebugOnScalaCompileTime")), TermName("xs")"#6666"
  //  ), TermName("head")"#9616"
  //  ) ), DefDef(Modifiers(METHOD | STABLE | ACCESSOR), TermName("head")"#6668"
  //  , List()
  //  , List()
  //  , TypeTree().setOriginal(Select(Ident("scala#22"
  //  ), "scala.Int#923"
  //  ) ), Select(This(TypeName("DebugOnScalaCompileTime")), TermName("head ")"#6667"
  //  ) ), Apply(Select(Select(Ident("scala#22"
  //  ), "scala.Predef#1299"
  //  ), TermName("println")"#6381"
  //  ), List(Apply(TypeApply(Select(Select(Ident("scala#22"
  //  ), "scala.Tuple2#1284"
  //  ), TermName("apply")"#10517"
  //  ), List(TypeTree(), TypeTree())
  //  ), List(Select(This(TypeName("DebugOnScalaCompileTime")), TermName("xs")"#6666"
  //  ), Select(This(TypeName("DebugOnScalaCompileTime")), TermName("head")"#6668"
  //  ) ) ) ) ) ) ) ) ) )


}
