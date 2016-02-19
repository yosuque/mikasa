package mikasa.service

import java.util.concurrent.{ ConcurrentHashMap, ConcurrentMap }

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.gracefulStop
import akka.routing.RoundRobinPool
import mikasa.actor.MikasaActor
import mikasa.logger.MikasaLogger

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

private[mikasa] object AkkaService extends MikasaLogger {

  private val TIMEOUT = 20.seconds

  private val system = ActorSystem("mikasa")
  private val actors: ConcurrentMap[String, ActorRef] = new ConcurrentHashMap[String, ActorRef]

  def registerActor(actor: MikasaActor): Unit = {
    val ref = system.actorOf(actor.props, actor.name)
    val _ = actors.put(actor.name, ref)
  }

  def registerActorWithRouter(actor: MikasaActor, poolSize: Int): Unit = {
    val ref = system.actorOf(actor.props.withRouter(RoundRobinPool(poolSize)), actor.name)
    val _ = actors.put(actor.name, ref)
  }

  def getActor(key: String): ActorRef = actors.get(key)

  def terminateActors(): Unit = {
    actors.values.foreach { actor =>
      Await.result(gracefulStop(actor, TIMEOUT), TIMEOUT)
    }
    val _ = Await.result(system.terminate(), TIMEOUT)
  }
}
