package mikasa.service.impl

import mikasa.logger.MikasaLogger
import mikasa.message.PublishMessage
import mikasa.service.SNSService
import com.amazonaws.services.sns.AmazonSNSClient

trait SNSServiceImpl
  extends SNSService
  with MikasaLogger {

  protected val snsClient: AmazonSNSClient

  def publishMessage(publishMessage: PublishMessage): Boolean = {
    try {
      snsClient.publish(publishMessage.topicArn, publishMessage.message)
      true
    } catch {
      case e: Exception => {
        error("sns publish message error.", e)
        false
      }
    }
  }
}
