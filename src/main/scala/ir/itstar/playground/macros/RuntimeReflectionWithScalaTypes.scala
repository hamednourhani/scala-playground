package ir.itstar.playground.macros

object RuntimeReflectionWithScalaTypes extends App {

  import scala.reflect.runtime.universe._

  final case class MetaDataT(key: String, value: String)

  final case class MyConfig[T](payload: T, metadata: List[MetaDataT])(implicit val tag: TypeTag[T])

  val myConfig = MyConfig(42, Nil)

  println(myConfig)

  println(myConfig.tag.tpe) // uses typeTag to convert JVM Object type to Scala type

  val mirror = runtimeMirror(myConfig.getClass.getClassLoader)

  val t = mirror.classSymbol(myConfig.getClass).toType

  println(t)



  val payloadSignature = t.member(TermName("payload")).typeSignatureIn(t)

  println(payloadSignature)

  val metadataSignature = t.member(TermName("metadata")).typeSignatureIn(t)

  println(metadataSignature)


}
