package mikasa.service

import mikasa.message.PublishMessage

trait SNSServiceCompornent {
  val snsService: SNSService
}

trait SNSService {

  def publishMessage(publishMessage: PublishMessage): Boolean
}
