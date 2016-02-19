package mikasa.util

import java.io.File

import com.amazonaws.auth.{ AWSCredentials, PropertiesCredentials }
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSClient

object AWS {

  def createAWSCredentials(awsCredentialsPath: String): AWSCredentials = {
    new PropertiesCredentials(new File(awsCredentialsPath))
  }

  def createSQSClient(awsCredentials: AWSCredentials, sqsEndPoint: String): AmazonSQSClient = {
    val sqsClient = new AmazonSQSClient(awsCredentials)
    sqsClient.setEndpoint(sqsEndPoint)
    sqsClient
  }

  def createSNSClient(awsCredentials: AWSCredentials, snsEndPoint: String): AmazonSNSClient = {
    val snsClient = new AmazonSNSClient(awsCredentials)
    snsClient.setEndpoint(snsEndPoint)
    snsClient
  }
}
