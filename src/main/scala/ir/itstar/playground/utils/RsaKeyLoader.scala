package ir.itstar.playground.utils


import java.nio.file.{Files, Paths}
import java.security.interfaces.RSAPublicKey
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security.{KeyFactory, PrivateKey, PublicKey}
import java.util.Base64

import javax.crypto.Cipher

import scala.util.Try


object RsaKeyLoader extends App {

  /**
    * generate rsa public private pair
    * openssl genrsa -out private_key.pem 2048
    * openssl rsa -pubout -in private_key.pem -out public_key.pem
    *
    * openssl pkcs8 -topk8 -in private_key.pem -inform pem -out private_key_pkcs8.pem -outform pem -nocrypt
    *
    */

  /**
    *
    * @param fileName : "private_key_pkcs8.pem"
    * @return
    */
  def loadPrivateKey(fileName:String): Option[PrivateKey] = {
    Try {
      val privateKeyContent =
        Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI))
          .map(_.toChar).mkString
      val cleanPrivateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "")
      val kf = KeyFactory.getInstance("RSA")
      val keySpecPKCS8: PKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder.decode(cleanPrivateKeyContent))
      val privateKey = kf.generatePrivate(keySpecPKCS8)
      privateKey
    }.toOption
  }

  val privateKey = loadPrivateKey("private_key_pkcs8.pem")

  /**
    *
    * @param fileName : "public_key.pem"
    * @return
    */
  def loadPublicKey(fileName:String): Option[PublicKey] = {
    Try {
      val publicKeyContent =
        Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fileName).toURI))
          .map(_.toChar).mkString
      val cleanPublicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
      val kf = KeyFactory.getInstance("RSA")
      val keySpecX509: X509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder.decode(cleanPublicKeyContent))
      val publicKey: RSAPublicKey = kf.generatePublic(keySpecX509).asInstanceOf[RSAPublicKey]
      publicKey
    }.toOption
  }

  val publicKey = loadPublicKey("public_key.pem")

  def encrypt(privateKey: PrivateKey, message: String): Option[Array[Byte]] = {
    Try {
      val cipher: Cipher = Cipher.getInstance("RSA")
      cipher.init(Cipher.ENCRYPT_MODE, privateKey)
      cipher.doFinal(message.getBytes())
    }.toOption
  }

  def decrypt(publicKey: PublicKey, encrypted: Array[Byte]): String = {
    val cipher: Cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, publicKey)
    val result = cipher.doFinal(encrypted)
    result.map(_.toChar).mkString
  }

  val encoded: Option[Array[Byte]] = privateKey match {
    case Some(pk) =>

      val message =  """
                       |{
                       | "status" : 123,
                       | "data" : {
                       |     "name" : "hamed",
                       |     "email" : "email@example.com",
                       |     "phone" : "23156454",
                       |     "cellphone" : "091215454"
                       |    }
                       |}
                     """.stripMargin

      val encrypted = encrypt(pk, message)
      println(encrypted.getOrElse("notEncrypted"))
      encrypted
    case None     =>
      println("private key not valid")
      None
  }

  val decoded: Option[String] = encoded.flatMap(e => publicKey.map((_, e))) match {
    case Some((pk, s)) =>
      val decrypted: String = decrypt(pk, s)
      println(decrypted)
      Some(decrypted)
    case None          =>
      None

  }

}