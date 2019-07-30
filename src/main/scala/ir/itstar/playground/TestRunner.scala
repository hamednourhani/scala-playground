package ir.itstar.playground

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}


object Util{

  lazy val sum = list.sum
  lazy val list = List(1,2,3,4)
  def show : String = "show"
  println(sum)
}

trait Car{
  def name : String
}

class Pride extends Car{
  override def name: String = "Pride"
}
class P206 extends Car{
  override def name: String = "206"
}

class User(val name:String,private val family:String,phone:Int){

  protected val code :Int = 0

  val car :Pride = new Pride

  val carName = car match {
    case i:Pride => i.name
    case j:P206 => j.name
    case _ => ""
  }

  car.name

  def fullName :String = name + " " + family



}


case class User2(name:String,family:String)

class APUser(n:String,f:String) extends User(n,f,0)

object TestRunner extends App{

  val user: User = new User("hamed","",91241351)

  val user2: User2 = User2("hame","")

  user2 match {
    case User2(name, _) =>name

  }

  val updatedUser2: User2 = user2.copy(name = "hamed")

  user.name
  println(user.fullName)

  val result: List[Int] = for{
    i <- List(1,2)
  } yield {
    i * 2
  }

  val note = ""

  note match {
    case i:String => println(s"$i is string")
  }


}




