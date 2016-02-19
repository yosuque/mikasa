package mikasa.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.util.control.Exception._

object Json {

  private val MAPPER = new ObjectMapper()
  MAPPER.registerModule(DefaultScalaModule)

  def toJson(any: AnyRef): Option[String] = {
    allCatch either {
      MAPPER.writeValueAsString(any)
    } match {
      case Right(c) => Some(c)
      case Left(t)  => None
    }
  }

  def toObject[T](json: String, clazz: Class[T]): Option[T] = {
    allCatch either {
      MAPPER.readValue(json, clazz)
    } match {
      case Right(c) => Some(c)
      case Left(t)  => None
    }
  }
}
