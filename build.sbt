organization := "net.abesto"

name := "Rougelike"

version := "0.1"

scalaVersion := "2.10.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
	"com.googlecode.lanterna" % "lanterna" % "2.1.7"
)
