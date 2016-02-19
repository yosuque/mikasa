package mikasa.service

import mikasa.message.DeleteMessage
import com.amazonaws.services.sqs.model.{ ReceiveMessageRequest, Message }

trait SQSServiceCompornent {
  val sqsService: SQSService
}

trait SQSService {

  def getQueueUrl(queueName: String): String

  def createRequest(queueUrl: String, maxNum: Int, waitTime: Int): ReceiveMessageRequest

  def receiveMessages(request: ReceiveMessageRequest): List[Message]

  def deleteMessage(deleteMessage: DeleteMessage): Boolean
}
