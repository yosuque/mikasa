package mikasa.actor

import akka.actor.{ Actor, ActorRef, Props }
import akka.pattern.gracefulStop
import akka.routing.RoundRobinPool
import mikasa.config.ActorConfig
import mikasa.logger.MikasaLogger
import mikasa.message._
import mikasa.service.{ SQSService, AkkaService }
import mikasa.util.Json

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

trait SQSReceiver
  extends Actor
  with MikasaLogger {

  protected val param: ActorConfig
  protected val sqsService: SQSService

  private val queueUrl = sqsService.getQueueUrl(param.queueName)

  private val receiveMessageRequest =
    sqsService.createRequest(queueUrl, param.maxNum, param.waitTime)

  private val worker = {
    val sqsDeleter = AkkaService.getActor("sqs-deleter")
    val snsPublisher = AkkaService.getActor("sns-publisher")

    context.actorOf(SQSWorker.props(queueUrl, param, sqsDeleter, snsPublisher, process _)
      .withRouter(RoundRobinPool(param.poolSize)), s"${param.actorName}-worker")
  }

  sys.addShutdownHook({
    val timeout = 20.seconds
    Await.result(gracefulStop(worker, timeout), timeout)
    context.system.stop(self)
    info(s"${param.actorName}-worker end.")
  })

  override def receive = {
    case Polling => {
      self ! Polling
      val messages = sqsService.receiveMessages(receiveMessageRequest)
      messages.foreach { message => worker ! Run(message) }
    }
  }

  def process(message: SQSMessage): Option[String]
}

private object SQSWorker {
  def props(queueUrl: String, param: ActorConfig,
            sqsDeleter: ActorRef, snsPublisher: ActorRef,
            process: (SQSMessage) => Option[String]) =
    Props(new SQSWorker(queueUrl, param, sqsDeleter, snsPublisher, process))
}

private class SQSWorker(queueUrl: String, param: ActorConfig,
                        sqsDeleter: ActorRef, snsPublisher: ActorRef,
                        process: (SQSMessage) => Option[String])
  extends Actor
  with MikasaLogger {

  override def receive = {

    case Run(message) => {
      val sqs = Json.toObject(message.getBody, classOf[SQSMessage])
      if (sqs.isEmpty) {
        warn("sqs message parse error. message=" + message)
        sqsDeleter ! Delete(DeleteMessage(queueUrl, message.getReceiptHandle))
      } else {
        try {
          val nextMessege = process(sqs.get)
          sqsDeleter ! Delete(DeleteMessage(queueUrl, message.getReceiptHandle))

          if (param.nextTopicArn.isDefined && nextMessege.isDefined) {
            snsPublisher ! Publish(PublishMessage(param.nextTopicArn.get, nextMessege.get))
          }
        } catch {
          case e: Exception => error("sqs worker error.", e)
        }
      }
    }
  }
}
