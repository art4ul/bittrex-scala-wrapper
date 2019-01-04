name := "bittrex-api"

organization := "com.art4ul"

homepage := Some(url("http://art4ul.com/"))

version := "1.0.1"

scalaVersion := "2.12.4"

crossScalaVersions := Seq("2.11.8", "2.12.4")

val akkaHttpVersion = "10.1.6"
val akkaVersion = "2.5.19"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.roundeights" %% "hasher" % "1.2.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion ,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion ,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.0.4" % Test
)