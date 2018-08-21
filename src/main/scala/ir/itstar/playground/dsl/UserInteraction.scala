package ir.itstar.playground.dsl

import ir.itstar.playground.monads._

//UI ADT
sealed trait UserInteraction[+A]

case class Tell[A](statement: A) extends UserInteraction[A]

case class Ask[A](question: A) extends UserInteraction[A]


object UserInteraction extends FreeFunctions {

  import Free._

  type Id[A] = A

  type InteractionDsl[A] = Free[UserInteraction, A]


  def tell[A](a: A): InteractionDsl[A] = lift(Tell(a))

  def ask[A](a: A): InteractionDsl[A] = lift(Ask(a))

  val executor: Executor[UserInteraction] = new Executor[UserInteraction] {
    override def exec[A](f: UserInteraction[A]): A = f match {
      case Tell(statement) =>
        println(statement)
        statement

      case Ask(question) =>
        println(question)
        val answer = scala.io.StdIn.readLine()
        answer.asInstanceOf[A]
    }

  }

  val consoleIo: NaturalTransformation[UserInteraction, Id]
  = new NaturalTransformation[UserInteraction, Id] {
    override def transform[A](f: UserInteraction[A]): Id[A] = f match {
      case Tell(st) =>
        println(st)
        st

      case Ask(st) =>
        println(st)
        val answer = scala.io.StdIn.readLine()
        answer.asInstanceOf[A]
    }
  }

}





