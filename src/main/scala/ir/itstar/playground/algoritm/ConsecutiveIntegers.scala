package ir.itstar.playground.algoritm

object ConsecutiveIntegers {

  def findLeast(target:Int,start:Int): Int ={
    def _findLeast(target:Int,current:Int) : Int ={
      if(current * (current + 1) >= target) current
      else _findLeast(target,current + 1)
    }
    _findLeast(target,start)
  }

  def findMost(target:Int,start:Int): Int ={
    def _findMost(target:Int,current:Int) : Int ={
      if((current * (current + 1)) <= target) current
      else _findMost(target,current - 1)
    }
    _findMost(target,start)
  }



  def solution(a: Int, b: Int): Int = {
    val maxA = math.sqrt(a).toInt
    val least = findLeast(a,maxA)

    val minB = math.sqrt(b).toInt
    val most = findMost(b,minB)

    val result = if(a == b) {
      if(least * (least + 1) == a) 1
      else 0
    } else {
      if (most == least) {
        if(least * (least + 1) == a) 1
        else if(most * (most + 1) == b) 1
        else 0
      } else most - least + 1
    }

    result


  }

}
