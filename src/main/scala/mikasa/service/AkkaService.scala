package mikasa.service

import java.util.concurrent.{ ConcurrentHashMap, ConcurrentMap }

import akka.actor.{ ActorRef, ActorSystem }
import akka.pattern.gracefulStop
import akka.routing.RoundRobinPool
import mikasa.actor.MikasaActor
import mikasa.logger.MikasaLogger
import mikasa.message.Polling

import scala.collection.JavaConversions._
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

private[mikasa] object AkkaService extends MikasaLogger {

  private val TIMEOUT = 20.seconds

  private val system = ActorSystem("mikasa")
  private val commons: ConcurrentMap[String, ActorRef] = new ConcurrentHashMap[String, ActorRef]
  private val receivers: ConcurrentMap[String, ActorRef] = new ConcurrentHashMap[String, ActorRef]

  def registerCommonActor(actor: MikasaActor, poolSize: Int): Unit = {
    val ref = system.actorOf(actor.props.withRouter(RoundRobinPool(poolSize)), actor.name)
    val _ = commons.putIfAbsent(actor.name, ref)
  }

  def registerReceiverActor(actor: MikasaActor): Unit = {
    val ref = system.actorOf(actor.props, actor.name)
    val _ = receivers.putIfAbsent(actor.name, ref)
  }

  def getCommonActor(key: String): ActorRef = commons.get(key)

  def getReceiverActor(key: String): ActorRef = receivers.get(key)

  def startReceiverActors(): Unit = {
    receivers.par foreach { case (name, receiver) =>
      receiver ! Polling
      info(s"${name} start.")
    }
  }

  def terminateActors(): Unit = {
    receivers.par foreach { case (name, receiver) =>
      Await.result(gracefulStop(receiver, TIMEOUT), TIMEOUT)
      info(s"${name} end.")
    }
    commons.values.par foreach { case (name, common) =>
      Await.result(gracefulStop(common, TIMEOUT), TIMEOUT)
    }
    val _ = Await.result(system.terminate(), TIMEOUT)
  }
}
