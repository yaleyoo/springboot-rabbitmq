package com.example.demo.connection;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author STEVE on 25/10/2022
 */
@Component
@PropertySource(value = "classpath:connection.properties", ignoreResourceNotFound = true)
public class AWSConn {
    private AmazonSQS sqs = null;

    @Value("${connection.AWS-ACCESS-KEY-ID}")
    private String awsAccessKeyId;
    @Value("${connection.AWS-SECRET-ACCESS-KEY}")
    private String awsSecretAccessKey;
    @Value("${connection.REGION}")
    private String region;


    public AmazonSQS getSqs() {
        if (this.sqs == null) {
            AWSCredentials credentials = new BasicAWSCredentials(
                    awsAccessKeyId,
                    awsSecretAccessKey
            );
            this.sqs = AmazonSQSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.fromName(region))
                    .build();
        }
        return this.sqs;
    }




//    public AWSConn(@Value("${com.example.demo.AWS_ACCESS_KEY_ID}") String AWS_ACCESS_KEY_ID,
//                   @Value("${com.example.demo.AWS_SECRET_ACCESS_KEY}") String AWS_SECRET_ACCESS_KEY,
//                   @Value("${com.example.demo.REGION}") String REGION) {
//        this.REGION = REGION;
//        this.credentials = new BasicAWSCredentials(
//                AWS_ACCESS_KEY_ID,
//                AWS_SECRET_ACCESS_KEY
//        );
//    }

}
