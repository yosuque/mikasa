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
    AkkaService.registerActorWithRouter(sqsDeleter, config.deleterPoolSize)
    AkkaService.registerActorWithRouter(snsPublisher, config.publisherPoolSize)
  }

  def register(actors: MikasaActor*): Unit = {
    actors.foreach { actor =>
      AkkaService.registerActor(actor)
    }
  }

  def terminate(): Unit = AkkaService.terminateActors()
}
