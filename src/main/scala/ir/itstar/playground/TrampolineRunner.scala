package ir.itstar.playground

import scala.annotation.tailrec
import scala.util.control.TailCalls._

object TrampolineRunner extends App {

  println("Trampoline runner")

  @tailrec
  def evenOrOdd(l: List[_], evenCheck: Boolean): Boolean = {
    l match {
      case Nil         =>
        if (evenCheck) true else false
      case (_ :: tail) => evenOrOdd(tail, !evenCheck)
    }
  }


  sealed trait EventOdd

  case class Done(result: Boolean) extends EventOdd

  case class Even(value: Int) extends EventOdd

  case class Odd(value: Int) extends EventOdd


  def even(t: Int): EventOdd = t match {
    case 0 => Done(true)
    case _ => odd(t - 1)
  }


  def odd(t: Int): EventOdd = t match {
    case 0 => Done(true)
    case _ => Even(t - 1)
  }

  @tailrec
  def run(eo: EventOdd): Boolean = eo match {
    case Done(r) => r
    case Even(v) => run(odd(v - 1))
    case Odd(v)  => run(even(v - 1))
  }

  println(run(Odd(122349234)))


  println("scala tailrec util")

  def oddTailRec(t: Int): TailRec[Boolean] = t match {
    case 0 => done(true)
    case v => tailcall(evenTailRec(v - 1))
  }

  def evenTailRec(t: Int): TailRec[Boolean] = t match {
    case 0 => done(true)
    case v => tailcall(oddTailRec(v - 1))
  }


  println(evenTailRec(1023232).result)


  println("abstract trampoline structure for computation")

  sealed trait Compute[+A]

  class Continue[A](n: => Compute[A]) extends Compute[A] {
    lazy val next: Compute[A] = n
  }

  case class DoneT[A](r: A) extends Compute[A]

  @tailrec
  def runT[T](c: Compute[T]): T = c match {
    case DoneT(r)         => r
    case con: Continue[T] => runT(con.next)
  }

  def evenT(i: Int): Compute[Boolean] = i match {
    case 0 => DoneT(true)
    case v => new Continue(oddT(v - 1))
  }

  def oddT(i: Int): Compute[Boolean] = i match {
    case 0 => DoneT(true)
    case v => new Continue(evenT(v - 1))
  }

  println(runT[Boolean](evenT(12983948)))

}
