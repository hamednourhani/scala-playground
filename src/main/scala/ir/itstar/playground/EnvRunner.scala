package ir.itstar.playground

object EnvRunner extends App{


  println(s"processors : ${sys.runtime.availableProcessors()}")

  println(s"freeMemory : ${sys.runtime.freeMemory()}")

  println(s"maxMemory : ${sys.runtime.maxMemory()}")

  println(s"totalMemory : ${sys.runtime.totalMemory()}")

  sys.env.foreach(tuple2 => println(s"${tuple2._1}:${tuple2._2}"))

}
