package mikasa.actor

import akka.actor.Props

trait MikasaActor {

  val name: String

  def props: Props
}
