name := "bittrex-api"

organization := "com.art4ul"

homepage := Some(url("http://art4ul.com/"))

version := "1.0.0"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-core" % "10.0.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
  "com.roundeights" %% "hasher" % "1.2.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % "test",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)