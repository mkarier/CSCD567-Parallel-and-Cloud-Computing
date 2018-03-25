import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class CleanMessages
{	   
	   public static BasicAWSCredentials awsCreds = new BasicAWSCredentials(access_key, secret_key);
	   public static void main(String[] args)
	   {
			cleanX();
			cleanO();
	   }
	   
	   public static void cleanX()
	   {
		   AmazonSQS sqs; //= AmazonSQSClientBuilder.defaultClient();
			String Q_NAME = "TicTacToeQueueX.fifo";
			String messageGroup = "TicTacToeQueueMessageGroup";
			String Q_URL;
			ReceiveMessageRequest receiveMessageRequest;
			SendMessageRequest sendMessageRequest;
			List<Message>  messages;
			sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-west-2").build();
			Map<String, String> attributes = new HashMap<String, String>();
			// A FIFO queue must have the FifoQueue attribute set to True
			attributes.put("FifoQueue", "true");
		   // Generate a MessageDeduplicationId based on the content, if the user doesn't provide a MessageDeduplicationId
		   attributes.put("ContentBasedDeduplication", "true");
		   // The FIFO queue name must end with the .fifo suffix
		   CreateQueueRequest createQueueRequest = new CreateQueueRequest(Q_NAME).withAttributes(attributes);
		   Q_URL= sqs.createQueue(createQueueRequest).getQueueUrl();
		   receiveMessageRequest = new ReceiveMessageRequest(Q_URL);
		   // Uncomment the following to provide the ReceiveRequestDeduplicationId
		   //receiveMessageRequest.setReceiveRequestAttemptId("1");
  			messages = sqs.receiveMessage(Q_URL).getMessages();
  			System.out.println("Size of message list " + messages.size());
  			for(int i = 0; i < messages.size(); )
  			{
  				String messageReceiptHandle = messages.get(i).getReceiptHandle();
  				System.out.println(messages.get(i).getBody());
  				sqs.deleteMessage(Q_URL, messageReceiptHandle); 
  				messages = sqs.receiveMessage(Q_URL).getMessages();
  			}
	   }//end of cleanX()
	   
	   public static void cleanO()
	   {
		    AmazonSQS sqs; //= AmazonSQSClientBuilder.defaultClient();
			String Q_NAME = "TicTacToeQueueO.fifo";
			String messageGroup = "TicTacToeQueueMessageGroup";
			String Q_URL;
			ReceiveMessageRequest receiveMessageRequest;
			SendMessageRequest sendMessageRequest;
			List<Message>  messages;
			sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-west-2").build();
			Map<String, String> attributes = new HashMap<String, String>();
			// A FIFO queue must have the FifoQueue attribute set to True
			attributes.put("FifoQueue", "true");
		   // Generate a MessageDeduplicationId based on the content, if the user doesn't provide a MessageDeduplicationId
		   attributes.put("ContentBasedDeduplication", "true");
		   // The FIFO queue name must end with the .fifo suffix
		   CreateQueueRequest createQueueRequest = new CreateQueueRequest(Q_NAME).withAttributes(attributes);
		   Q_URL= sqs.createQueue(createQueueRequest).getQueueUrl();
		   receiveMessageRequest = new ReceiveMessageRequest(Q_URL);
		   // Uncomment the following to provide the ReceiveRequestDeduplicationId
		   //receiveMessageRequest.setReceiveRequestAttemptId("1");
			messages = sqs.receiveMessage(Q_URL).getMessages();
			System.out.println("Size of message list " + messages.size());
			for(int i = 0; i < messages.size(); )
			{
				String messageReceiptHandle = messages.get(i).getReceiptHandle();
				System.out.println(messages.get(i).getBody());
				sqs.deleteMessage(Q_URL, messageReceiptHandle);
				messages = sqs.receiveMessage(Q_URL).getMessages();
  			}
	   }//end of cleanO
}
