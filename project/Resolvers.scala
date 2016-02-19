import sbt._

object Resolvers {

  val mikasaReslv = Seq(
    "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
    "Local Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
  )
}
