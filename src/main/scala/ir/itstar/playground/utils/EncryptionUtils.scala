package ir.itstar.playground.utils

import java.nio.file.{Files, Paths}
import java.security.interfaces.RSAPublicKey
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security.{KeyFactory, PrivateKey, PublicKey, Signature}
import java.util.Base64

import javax.crypto.Cipher

import scala.util.Try


object EncryptionUtils {

  private val optional_key: Option[PublicKey] = loadPublicKey("keys/flights_key.pem")

  val example_key: PublicKey = optional_key match {
    case Some(pk) =>
      println("keys loaded successfully")
      pk
    case None     =>
      throw new Exception("can not load flights private key")
  }

  private val optional_flights_privateKey: Option[PrivateKey] =
    loadPrivateKey("keys/flights_private_key_pkcs8.pem")

  val flights_private_key: PrivateKey = optional_flights_privateKey match {
    case Some(pk) =>
      println("keys loaded successfully")
      pk
    case None     =>
      throw new Exception("can not load flights private key")
  }


  /**
    * generate rsa public private pair
    *
    * openssl genrsa -out private_key.pem 2048
    * openssl rsa -pubout -in private_key.pem -out public_key.pem
    *
    * openssl pkcs8 -topk8 -in private_key.pem -inform pem -out private_key_pkcs8.pem -outform pem -nocrypt
    */

  /**
    * @param fileName : "flights_private_key_pkcs8.pem"
    * @return
    */
  private def loadPrivateKey(fileName: String): Option[PrivateKey] = {
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

  /**
    *
    * @param fileName : "flights_key.pem"
    * @return
    */
  private def loadPublicKey(fileName: String): Option[PublicKey] = {
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


  def encrypt(privateKey: PrivateKey, message: String): Option[Array[Byte]] = {
    Try {
      val cipher: Cipher = Cipher.getInstance("RSA")
      cipher.init(Cipher.ENCRYPT_MODE, privateKey)
      val encrypted = cipher.doFinal(message.getBytes())
      Base64.getEncoder.encode(encrypted)
    }.toOption
  }

  def decrypt(publicKey: PublicKey, encrypted: Array[Byte]): String = {
    val message = Base64.getDecoder.decode(encrypted)
    val cipher: Cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.DECRYPT_MODE, publicKey)
    val result = cipher.doFinal(message)
    result.map(_.toChar).mkString
  }

  def sign(privateKey: PrivateKey, message: String): Option[String] = {
    Try {
      val privateSign = Signature.getInstance("SHA1withRSA")
      privateSign.initSign(privateKey)
      privateSign.update(message.getBytes("UTF-8"))
      val signatureBytes: Array[Byte] = privateSign.sign()
      Base64.getEncoder.encodeToString(signatureBytes)
    }.toOption
  }

  def verify(publicKey: PublicKey, message: String, signature: String): Boolean = {
    Try {
      val publicSignature = Signature.getInstance("SHA1withRSA")
      publicSignature.initVerify(publicKey)
      publicSignature.update(message.getBytes("UTF-8"))
      val privateSignature = Base64.getDecoder.decode(signature)
      println(privateSignature)
      println(publicSignature)
      publicSignature.verify(privateSignature)
    }.toOption match {
      case Some(true)  => true
      case Some(false) => false
      case None        =>
        println("error while verifying message signature")
        false
    }
  }
}