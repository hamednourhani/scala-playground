package ir.itstar.playground

import ir.itstar.playground.dsl.UserInteraction._

object FreeMonadRunner extends App {

  val program = for {
    _ <- tell("greeting")
    name <- ask("what is your name")
    _ <- tell(s"hi, $name")
  } yield ()

  println("run free monad : ")
  transformFree(program, consoleIo)

  println("run free monad with transformation:")
  runFree(program, executor)

}