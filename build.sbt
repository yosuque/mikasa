import Commons._

import Dependencies._

import Resolvers._

lazy val mikasa = (project in file("."))
  .settings(scalariformSettings: _*)
  .settings(mikasaBuildSettings: _*)
  .settings(packAutoSettings: _*)
  .settings(name := mikasaProjName)
  .settings(resolvers ++= mikasaReslv)
  .settings(libraryDependencies ++= commonDeps)
  .settings(libraryDependencies ++= awsDeps)
  .settings(libraryDependencies ++= akkaDeps)
