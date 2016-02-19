import sbt._
import sbt.Keys._
import Resolvers._
import Formatting._

object Commons {

  val mikasaProjName = "mikasa"

  lazy val mikasaBuildSettings = Seq(
    version := "1.0.0",
    organization := "mikasa",
    scalaVersion := "2.11.7",
    scalacOptions := Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Ywarn-value-discard"
    ),
    scalacOptions in Test ++= Seq("-Yrangepos"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    resolvers ++= mikasaReslv,
    mikasaScalariformSettings
  )
}
