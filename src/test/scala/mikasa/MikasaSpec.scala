package mikasa

import akka.actor.Props
import mikasa.actor.{ MikasaActor, SQSReceiver }
import mikasa.config.{ ActorConfig, MikasaConfig }
import mikasa.message.SQSMessage
import mikasa.service.impl.{ SNSServiceImpl, SQSServiceImpl }
import mikasa.service.{ SQSServiceCompornent, SNSServiceCompornent, SNSService, SQSService }
import mikasa.util.{ AWS, Eval }
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSClient
import org.specs2.mutable.Specification

class MikasaSpec extends Specification {

  "Mikasa" should {

    "init-register-terminate" in {

      val mikasa = new Mikasa with SQSServiceRegistry with SNSServiceRegistry with ConfigRegistry

      mikasa.init()
      mikasa.register(SpecReciever)

      Thread.sleep(3000L)

      mikasa.start()

      Thread.sleep(3000L)

      mikasa.terminate()

      ok
    }
  }
}

private trait ConfigRegistry {
  val config = Eval.fromFileName[MikasaConfig]("src/test/resources/SpecConfig.scala")
  val credentials = AWS.createAWSCredentials("src/test/resources/spec-credentials.properties")
  val sqsClient: AmazonSQSClient = AWS.createSQSClient(credentials, "spec-sqs-endpoint")
  val snsClient: AmazonSNSClient = AWS.createSNSClient(credentials, "spec-sns-endpoint")
  val param: ActorConfig = ActorConfig(actorName = "spec-reciever", queueName = "spec-reciever-queue")
}

private trait SQSServiceRegistry extends SQSServiceCompornent {
  val sqsService: SQSService = new SQSServiceImpl with ConfigRegistry
}

private trait SNSServiceRegistry extends SNSServiceCompornent {
  val snsService: SNSService = new SNSServiceImpl with ConfigRegistry
}

private object SpecReciever extends MikasaActor with SQSServiceRegistry with ConfigRegistry {
  override val name: String = "spec-reciever"
  override def props: Props = Props(new SpecReciever(param, sqsService))
}

private class SpecReciever(override val param: ActorConfig,
                           override val sqsService: SQSService) extends SQSReceiver {
  override def process(message: SQSMessage): Option[String] = {
    println("hello")
    None
  }
}
