package com.example.demo.rabbitmq;

import com.example.demo.connection.RabbitConn;
import com.rabbitmq.client.AMQP;
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
public class BroadCastProducer extends AbstractRabbitProducer{

    private final String exchangeName = "amq.fanout";
    public static final String[] queues = {"queue1", "queue2", "queue3"};

    @Autowired
    public BroadCastProducer(RabbitConn rabbitConn) {
        super(rabbitConn);
    }

    @Override
    public void sendSimpleMsg(String msg, String routingKey) throws IOException, TimeoutException {
        Channel channel = this.getChannel();

        channel.basicPublish(this.exchangeName, "", null, msg.getBytes());

    }

    @Override
    protected void prepChannel(Channel channel) throws IOException, TimeoutException {
        channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.FANOUT, true);

        for (String q : queues) {
            /**
             * 参数1：队列名称
             * 参数2：是否定义持久化队列
             * 参数3：是否独占本次连接
             * 参数4：是否在不使用的时候自动删除队列
             * 参数5：队列其它参数
             */
            channel.queueDeclare(q, true, false, false, null);
            /**
             * arg1: queue name
             * arg2: exchange name
             * arg3: routing key
             * arg4: arguments
             */
            channel.queueBind(q, this.exchangeName, "", null);
        }

    }
}
