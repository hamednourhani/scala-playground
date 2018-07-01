package ir.itstar.playground

object FreeMonadRunner extends App {

  println("free monad runner")

  println(s"from : https://medium.com/@olxc/free-monads-explained-pt-1-a5c45fbdac30")


  //  println("Greetings!")
  //  println("What is your name?")
  //  val name = scala.io.StdIn.readLine()
  //  println(s"Hi $name!")


  sealed trait UserInteraction[+A]

  case class Tell[A](statement: A) extends UserInteraction[A]

  case class Ask[A](question: A) extends UserInteraction[A]


  val flow = List(
    Tell("Greeting"),
    Ask("What is your name?"),
    Tell("hi, have a nice time")
  )

  def execute[A](ui: UserInteraction[A]): A = ui match {
    case Tell(a) =>
      println(a)
      a

    case Ask(a) =>
      println(a)
      val q = scala.io.StdIn.readLine().asInstanceOf[A]
      println(q)
      q
  }

  def run[T](l: List[UserInteraction[T]]): Unit = l.foreach(execute)


  //  run(flow)


  //Free monad AST

  sealed trait Free[F[_], A] {

    def flatMap[B](f: A => Free[F, B]): Free[F, B] = this match {
      case Return(a)          => f(a)
      case FlatMap(sub, cont) => FlatMap(sub, cont andThen (_ flatMap f))
    }

    def map[B](f: A => B): Free[F, B] = flatMap(a => Return(f(a)))

  }

  case class Return[F[_], A](a: A) extends Free[F, A]

  case class FlatMap[F[_], I, A](sub: F[I], cont: I => Free[F, A]) extends Free[F, A]


  //  FlatMap(Tell("Greeting!"), (_) =>
  //    FlatMap(Ask("what is your name"), (name) =>
  //      Return(Tell(s"Hi, $name")))
  //  )


  def toFree[A](fa: UserInteraction[A]): Free[UserInteraction, A] = FlatMap(fa, Return.apply)


  val program = for {
    _ <- toFree(Tell("Greeting!"))
    name <- toFree(Ask("what is your name"))
    _ <- toFree(Tell(s"Hi, $name"))
  } yield ()


}
