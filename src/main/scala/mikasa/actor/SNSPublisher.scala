package mikasa.actor

import akka.actor.{ Actor, Props }
import mikasa.message.Publish
import mikasa.service.{ SNSService, SNSServiceCompornent }

private[mikasa] trait SNSPublisherCompornent
  extends MikasaActor
  with SNSServiceCompornent {

  private val sns = snsService

  override val name = "sns-publisher"

  override def props = Props(new SNSPublisher {
    override val snsService: SNSService = sns
  })
}

private[mikasa] trait SNSPublisher
  extends Actor
  with SNSServiceCompornent {

  override def receive = {
    case Publish(publishMessage) => {
      val _ = snsService.publishMessage(publishMessage)
    }
  }
}
