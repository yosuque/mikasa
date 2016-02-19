package mikasa.config

case class ActorConfig(actorName: String = "",
                       queueName: String = "",
                       poolSize: Int = 4,
                       maxNum: Int = 10,
                       waitTime: Int = 10,
                       nextTopicArn: Option[String] = None)
