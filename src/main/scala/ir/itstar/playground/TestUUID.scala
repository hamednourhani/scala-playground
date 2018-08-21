package ir.itstar.playground

import java.util.UUID

object TestUUID extends App{

  def generateUUID(trackerCode:String): String = {
    val other = "-0000-0000-0000-000000000000"
    if(trackerCode.length < 9){
      s"$trackerCode${Range(0,8 - trackerCode.length).map(_ => 0).mkString("")}$other"
    }else{
      s"${trackerCode.slice(0,8)}$other"
    }
  }

  println(UUID.fromString("b2b00000-a000-0-0-0"))
  println(UUID.fromString("a-e-0-0-0"))
  println(UUID.fromString("c-0-0-0-0"))
  println(UUID.fromString("e-0-0-0-0"))
  println(UUID.fromString("ac-0-0-0-0"))
  println(UUID.fromString("def-0-0-0-0"))


}
