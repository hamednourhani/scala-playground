name := "playground"

version := "0.1"

scalaVersion := "2.12.6"

scalacOptions ++= Seq("-feature")


lazy val akkaHttpVersion = "10.0.10"
lazy val akkaVersion = "2.5.6"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "org.reactivemongo" %% "reactivemongo" % "0.16.1",
  "com.lightbend.akka" %% "akka-stream-alpakka-mongodb" % "1.0-M1",
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "com.typesafe.scala-logging"  %% "scala-logging"        % "3.7.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.4.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.ibm.icu" % "icu4j" % "60.2",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.scalamock" %% "scalamock" % "4.1.0" % "test",
  "org.scalaz" %% "scalaz-core" % "7.2.23",
//  "ir.itstar" %% "tiny-macros" % "0.0.1",
  "com.typesafe.slick" %% "slick" % "3.2.2",
  "com.h2database" % "h2" % "1.4.185",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
   "io.spray" %%  "spray-json" % "1.3.4",
  "commons-codec" % "commons-codec" % "1.11",
  "com.typesafe.akka" %% "akka-http-xml" % "10.0.9"
)
