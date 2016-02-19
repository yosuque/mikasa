package mikasa.util

import java.io.File

import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox

object Eval {

  def apply[T](string: String): T = {
    val toolbox = currentMirror.mkToolBox()
    val tree = toolbox.parse(string)
    toolbox.eval(tree).asInstanceOf[T]
  }

  def fromFile[T](file: File): T = apply(scala.io.Source.fromFile(file).mkString(""))

  def fromFileName[T](fileName: String): T = fromFile(new File(fileName))
}
