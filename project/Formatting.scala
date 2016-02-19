import com.typesafe.sbt.SbtScalariform.ScalariformKeys

import scalariform.formatter.preferences._

object Formatting {

  lazy val mikasaScalariformSettings = ScalariformKeys.preferences :=
    FormattingPreferences()
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(IndentPackageBlocks, false)
      .setPreference(IndentSpaces, 2)
      .setPreference(PreserveDanglingCloseParenthesis, true)
}
