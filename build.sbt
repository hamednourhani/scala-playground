name := "playground"

version := "0.1"

scalaVersion := "2.12.6"

scalacOptions ++= Seq("-feature","-Ypartial-unification")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)


lazy val akkaHttpVersion = "10.0.10"
lazy val akkaVersion = "2.5.22"
lazy val zioVersion = "1.0-RC4"

val catsVersion = "1.5.0"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "org.reactivemongo" %% "reactivemongo" % "0.16.1",
  "com.lightbend.akka" %% "akka-stream-alpakka-mongodb" % "1.0-M1",
  "org.slf4j" % "slf4j-simple" % "1.7.25",
  "com.typesafe.scala-logging"  %% "scala-logging"        % "3.7.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.4.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-typed" % "2.5.8",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka"          %% "akka-slf4j"     % "2.4.12",
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
  "com.typesafe.akka" %% "akka-http-xml" % "10.0.9",
  "com.chuusai" %% "shapeless" % "2.3.3",
  "org.typelevel" %% "cats-core" % catsVersion,
  "com.github.julien-truffaut" %%  "monocle-core"  % catsVersion,
  "com.github.julien-truffaut" %%  "monocle-macro" % catsVersion,
  "com.github.julien-truffaut" %%  "monocle-law"   % catsVersion % "test",
  "org.scalaz" %% "scalaz-zio" % zioVersion,
  "org.scalaz" %% "scalaz-zio-streams" % zioVersion,
  "com.github.mpilquist" %% "simulacrum" % "0.19.0",
  "com.propensive" %% "magnolia" % "0.11.0"

)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
