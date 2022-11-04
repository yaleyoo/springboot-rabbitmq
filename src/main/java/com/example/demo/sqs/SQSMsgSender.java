package com.example.demo.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.example.demo.connection.AWSConn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author STEVE on 25/10/2022
 */
@Component
@PropertySource(value = "classpath:connection.properties", ignoreResourceNotFound = true)
public class SQSMsgSender {
    private final AmazonSQS awsSqs;
    @Value("${connection.QUEUE-URL}")
    private String queueURL;

    @Autowired
    public SQSMsgSender(AWSConn awsConn) {
        this.awsSqs = awsConn.getSqs();
    }

    public void sendMessage(String msg) {
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("AttributeOne", new MessageAttributeValue()
                .withStringValue("This is an attribute")
                .withDataType("String"));

        SendMessageRequest sendMessageStandardQueue = new SendMessageRequest()
                .withQueueUrl(queueURL)
                .withMessageBody(msg)
                .withDelaySeconds(30)
                .withMessageAttributes(messageAttributes);

        SendMessageResult result = this.awsSqs.sendMessage(sendMessageStandardQueue);
        System.out.println(result.getMessageId());
    }

}
