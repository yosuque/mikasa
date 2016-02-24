package mikasa

import mikasa.actor.{ SNSPublisherCompornent, SQSDeleterCompornent, MikasaActor }
import mikasa.config.MikasaConfig
import mikasa.logger.MikasaLogger
import mikasa.service._

trait Mikasa
  extends SQSServiceCompornent
  with SNSServiceCompornent
  with MikasaLogger {

  protected val config: MikasaConfig

  private val sqs = sqsService
  private val sns = snsService

  def init(): Unit = {
    val sqsDeleter = new SQSDeleterCompornent {
      override val sqsService: SQSService = sqs
    }
    val snsPublisher = new SNSPublisherCompornent {
      override val snsService: SNSService = sns
    }
    AkkaService.registerCommonActor(sqsDeleter, config.deleterPoolSize)
    AkkaService.registerCommonActor(snsPublisher, config.publisherPoolSize)

    info("mikasa init.")
  }

  def register(actors: MikasaActor*): Unit = {
    actors foreach { AkkaService.registerReceiverActor }
  }

  def start(): Unit = AkkaService.startReceiverActors()

  def terminate(): Unit = {
    AkkaService.terminateActors()
    info("mikasa end.")
  }
}
