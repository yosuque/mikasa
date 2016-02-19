package mikasa.message

import com.fasterxml.jackson.annotation.JsonProperty

case class SQSMessage(@JsonProperty("Type") sqsType: String,
                      @JsonProperty("MessageId") messageId: String,
                      @JsonProperty("TopicArn") topicArn: String,
                      @JsonProperty("Message") message: String,
                      @JsonProperty("Timestamp") timestamp: String,
                      @JsonProperty("SignatureVersion") signatureVersion: String,
                      @JsonProperty("Signature") signature: String,
                      @JsonProperty("SigningCertURL") signingCertURL: String,
                      @JsonProperty("UnsubscribeURL") unsubscribeURL: String)
