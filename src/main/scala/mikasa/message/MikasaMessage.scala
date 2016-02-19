package mikasa.message

import com.amazonaws.services.sqs.model.Message

sealed trait MikasaMessage
case object Polling extends MikasaMessage
case class Run(message: Message) extends MikasaMessage
case class Publish(sendMessage: PublishMessage) extends MikasaMessage
case class Delete(deleteMessage: DeleteMessage) extends MikasaMessage
