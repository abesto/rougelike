organization := "net.abesto"

name := "Rougelike"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

val slf4jVersion = "1.7.6"
val logbackVersion = "1.1.1"

libraryDependencies ++= Seq(
	"com.googlecode.lanterna" % "lanterna" % "2.1.7",   // Terminal-like window
  "org.specs2" %% "specs2" % "2.3.8",
  // Logging
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion
)
