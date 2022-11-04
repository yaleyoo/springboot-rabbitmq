package com.example.demo.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.example.demo.connection.AWSConn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * @author STEVE on 26/10/2022
 */
@Component
@PropertySource(value = "classpath:connection.properties", ignoreResourceNotFound = true)
public class SQSMsgConsumer {
    private final AmazonSQS awsSqs;
    @Value("${connection.QUEUE-URL}")
    private String queueURL;
    private Vector<String> ids = new Vector<>();

    @Autowired
    public SQSMsgConsumer(AWSConn awsConn) {
        this.awsSqs = awsConn.getSqs();
    }

    public void consumeMsg() {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueURL)
                .withWaitTimeSeconds(10)
                .withMaxNumberOfMessages(1);
        List<Message> messages = awsSqs.receiveMessage(receiveMessageRequest).getMessages();

        if (messages.size() == 0) return;

        Message msg = messages.get(0);
        ids.add(msg.getMessageId());
        System.out.println("Processing: " + msg.getBody() + " , have " + ids.size() + " items processing...");


        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest()
                .withQueueUrl(queueURL)
                .withReceiptHandle(msg.getReceiptHandle());
        DeleteMessageResult result = awsSqs.deleteMessage(deleteMessageRequest);
        ids.remove(msg.getMessageId());
        System.out.println("DELETED" + result);

    }


}
