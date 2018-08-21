package ir.itstar.playground

object TestURLDecoder extends App{
  import spray.json._
  import DefaultJsonProtocol._


  val s = "startupData=%7B%22authToken%22%3A%22gegsfTF75xuIbNGYTrzISVLs0uoa%2FtKbC7lEMgHtOBLGwLoRNrJWwrwue2i59mMTNs3tnyrCXQxDI0%2B0ql2AH6I8DEdcez%2FioYSycS0EB1KLNH5TGiIvoVtCL6y3fLL7%22%2C%22platfom%22%3A%22android%22%2C%22appVersion%22%3A%222.6.3%22%2C%22lang%22%3A%22fa%22%2C%22additionalData%22%3A%7B%22setMobile%22%3A%22%22%2C%22setAmount%22%3A%22%22%2C%22helpId%22%3A0%7D%7D"

  val body = java.net.URLDecoder.decode(s,"UTF-8")

  val source = body.replace("startupData=","")



  case class APInitialData(authToken:String,platfom:String,appVersion:String,lang:String,additionalData:APAdditionalData)

  case class APAdditionalData(setMobile : String,setAmount:String,helpId: Int)

  implicit val apAdditionalDataFormat: RootJsonFormat[APAdditionalData] = jsonFormat3(APAdditionalData)
  implicit val aPInitialDataFormat: RootJsonFormat[APInitialData] = jsonFormat5(APInitialData)

  val jsonAst = source.parseJson.convertTo[APInitialData] // or JsonParser(source)
  println(jsonAst)

}
