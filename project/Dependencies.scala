import sbt._

object Dependencies {

  val commonDeps = Seq(
    "org.specs2" %% "specs2-core" % "3.7.1" % "test",
    "org.scalaj" %% "scalaj-time" % "0.8",
    "com.twitter" %% "util-eval" % "6.32.0",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.3",
    "org.apache.commons" % "commons-lang3" % "3.4",
    "com.google.guava" % "guava" % "19.0",
    "ch.qos.logback" % "logback-classic" % "1.1.5"
  )

  val awsDeps = Seq(
    "com.amazonaws" % "aws-java-sdk-sns" % "1.10.52",
    "com.amazonaws" % "aws-java-sdk-sqs" % "1.10.52"
  )

  val akkaDeps = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.2",
    "com.typesafe.akka" %% "akka-testkit" % "2.4.2" % "test"
  )
}
