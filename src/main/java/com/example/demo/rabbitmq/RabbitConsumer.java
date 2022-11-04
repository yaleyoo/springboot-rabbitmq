package com.example.demo.rabbitmq;

import com.example.demo.connection.RabbitConn;
import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author STEVE on 1/11/2022
 */
public class RabbitConsumer {
    private final String queueName;
    private final ConnectionFactory connectionFactory;

    @Autowired
    public RabbitConsumer(RabbitConn rabbitConn, String queueName) throws IOException, TimeoutException {
        this.queueName = queueName;
        this.connectionFactory = rabbitConn.getConnectionFactory();

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(this.queueName, true, false, false, null);

        DefaultConsumer consumer = new DefaultConsumer(channel){
            /**
             * consumerTag 消息者标签，在channel.basicConsume时候可以指定
             * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志(收到消息失败后是否需要重新发送)
             * properties 属性信息
             * body 消息
             */
            @SneakyThrows
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println(this.getClass().getSimpleName() + "Working on the task...");
                    Thread.sleep(10000);
                    //路由key
                    System.out.println("路由key为：" + envelope.getRoutingKey());
                    //交换机
                    System.out.println("交换机为：" + envelope.getExchange());
                    //消息id
                    System.out.println("消息id为：" + envelope.getDeliveryTag());
                    //收到的消息
                    System.out.println("接收到的消息为：" + new String(body, "utf-8"));
                } finally {
                    this.getChannel().basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复
         会删除消息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(this.queueName, false, consumer);

        System.out.println("Consumer " + this.toString() + " ready, listening queue: " + this.queueName);
    }

}
