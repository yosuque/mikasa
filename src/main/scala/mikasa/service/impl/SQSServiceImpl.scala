package mikasa.service.impl

import mikasa.logger.MikasaLogger
import mikasa.message.DeleteMessage
import mikasa.service.SQSService
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.{ DeleteMessageRequest, Message, ReceiveMessageRequest }

import scala.collection.JavaConversions._

trait SQSServiceImpl
  extends SQSService
  with MikasaLogger {

  protected val sqsClient: AmazonSQSClient

  def getQueueUrl(queueName: String): String = {
    sqsClient.getQueueUrl(queueName).getQueueUrl
  }

  def createRequest(queueUrl: String, maxNum: Int, waitTime: Int): ReceiveMessageRequest = {
    new ReceiveMessageRequest(queueUrl)
      .withMaxNumberOfMessages(maxNum).withWaitTimeSeconds(waitTime)
  }

  def receiveMessages(request: ReceiveMessageRequest): List[Message] = {
    try {
      sqsClient.receiveMessage(request).getMessages.toList
    } catch {
      case e: Exception => {
        error("sqs receive message error.", e)
        List.empty[Message]
      }
    }
  }

  def deleteMessage(deleteMessage: DeleteMessage): Boolean = {
    val request = new DeleteMessageRequest(deleteMessage.queueUrl, deleteMessage.receiptHandle)
    try {
      sqsClient.deleteMessage(request)
      true
    } catch {
      case e: Exception => {
        error("sqs delete message error.", e)
        false
      }
    }
  }
}
