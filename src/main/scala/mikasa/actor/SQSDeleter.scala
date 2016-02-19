package mikasa.actor

import akka.actor.{ Actor, Props }
import mikasa.message.Delete
import mikasa.service.{ SQSService, SQSServiceCompornent }

private[mikasa] trait SQSDeleterCompornent
  extends MikasaActor
  with SQSServiceCompornent {

  private val sqs = sqsService

  override val name = "sqs-deleter"

  override def props = Props(new SQSDeleter {
    override val sqsService: SQSService = sqs
  })
}

private[mikasa] trait SQSDeleter
  extends Actor
  with SQSServiceCompornent {

  override def receive = {
    case Delete(deleteMessage) => {
      val _ = sqsService.deleteMessage(deleteMessage)
    }
  }
}
