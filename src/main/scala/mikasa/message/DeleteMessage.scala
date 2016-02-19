package mikasa.message

case class DeleteMessage(queueUrl: String, receiptHandle: String)
