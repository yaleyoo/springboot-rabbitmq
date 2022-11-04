package com.example.demo.rabbitmq;

import com.example.demo.connection.RabbitConn;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author STEVE on 3/11/2022
 */
@Component
public class RoutingProducer extends AbstractRabbitProducer{

    private final String exchangeName = "amq.direct";
    public static String[] routingKeys = {"key1", "key2", "key3"};
    public static final String queueNameSuffix = "_queue";

    @Autowired
    public RoutingProducer(RabbitConn rabbitConn) {
        super(rabbitConn);
    }


    @Override
    public void sendSimpleMsg(String msg, String routingKey) throws IOException, TimeoutException {
        Channel channel = this.getChannel();


        channel.basicPublish(this.exchangeName, routingKey, null, msg.getBytes());
    }

    @Override
    protected void prepChannel(Channel channel) throws IOException, TimeoutException {
        channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.DIRECT, true);

        /**
         * Bind routing key with queue
         */
        for (String k : routingKeys) {
            channel.queueDeclare(k+ queueNameSuffix, true, false, false, null);
            channel.queueBind(k+ queueNameSuffix, this.exchangeName, k);
        }
    }
}
