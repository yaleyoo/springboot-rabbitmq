package com.example.demo.rabbitmq;

import com.example.demo.connection.RabbitConn;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author STEVE on 4/11/2022
 */
@Component
public class TopicProducer extends AbstractRabbitProducer{
    private final String exchangeName = "amq.topic";
    public static String[] topics = {"a.b", "a.c", "b.c", "#", "*.b", "a.#"};
    public static String queueSuffix = "_queue";

    @Autowired
    public TopicProducer(RabbitConn rabbitConn) {
        super(rabbitConn);
    }

    @Override
    public void sendSimpleMsg(String msg, String routingKey) throws IOException, TimeoutException {
        Channel channel = this.getChannel();

        channel.basicPublish(this.exchangeName, routingKey, null, msg.getBytes());
    }

    @Override
    protected void prepChannel(Channel channel) throws IOException, TimeoutException {
        channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC, true);

        for (String t : topics) {
            channel.queueDeclare(t + queueSuffix, true, false, false, null);
            channel.queueBind(t + queueSuffix, this.exchangeName, t);
        }
    }
}
