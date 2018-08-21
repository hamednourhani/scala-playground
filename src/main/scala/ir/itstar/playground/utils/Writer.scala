package ir.itstar.playground.utils

class Writer[W, T](private val _value: T, private val history: List[W] = Nil) {
  self =>

  def run: (T, List[W]) = (_value, history)

  def written: List[W] = history

  def value: T = _value

  def write(w: W): Writer[W, T] = new Writer[W, T](self._value, w :: self.history)

  def writeIf(f: T => Boolean)(w: W): Writer[W, T] = if (f(_value)) self.write(w) else self

  def map[K](f: T => K) = new Writer[W, K](f(_value), self.history)

  def flatMap[K](f: T => Writer[W, K]): Writer[W, K] = {
    val temp = f(_value)
    new Writer[W, K](temp._value, self.history ::: temp.written)
  }

  def mapWritten[K](f: W => K) = new Writer[K, T](_value, history.map(f))

}

object Writer {
  def apply[W, T](t: T, history: List[W] = Nil) = new Writer[W, T](t, history)

  def empty[W]: Writer[W, Unit] = new Writer[W, Unit](Unit, Nil)
}


