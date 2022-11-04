package com.example.demo.rabbitmq;

import com.example.demo.connection.RabbitConn;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author STEVE on 3/11/2022
 */
@Component
public class WorkerProducer extends AbstractRabbitProducer {

    public static final String QUEUE_NAME = "SIMPLE_Q";

    @Autowired
    public WorkerProducer(RabbitConn rabbitConn) {
        super(rabbitConn);
    }

    @Override
    public void sendSimpleMsg(String msg, String routingKey) throws IOException, TimeoutException {

        Channel channel = this.getChannel();
        /**
         * arg1: exchange
         * arg2: queue name
         * arg3: basic properties (https://www.rabbitmq.com/publishers.html)
         * arg4: message body
         */
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        /**
         * example with properties
        channel.basicPublish("", QUEUE_NAME, new AMQP.BasicProperties.Builder()
                        .contentType("text/plain")
                        .deliveryMode(2)
                        .priority(1)
                        .userId("bob")
                        .build(),
                msg.getBytes());
         */
        System.out.println("Message sent:" + msg);
    }

    @Override
    protected void prepChannel(Channel channel) throws IOException, TimeoutException {
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

    }
}
