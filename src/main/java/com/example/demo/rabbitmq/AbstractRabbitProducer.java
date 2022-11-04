package com.example.demo.rabbitmq;

import com.example.demo.connection.RabbitConn;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author STEVE on 1/11/2022
 */
public abstract class AbstractRabbitProducer {
    private final ConnectionFactory connectionFactory;
    private Connection connection;
    private Channel channel;

    public AbstractRabbitProducer(RabbitConn rabbitConn) {
        this.connectionFactory = rabbitConn.getConnectionFactory();
    }

    protected Connection getConnection() throws IOException, TimeoutException {
        if (this.connection == null || !this.connection.isOpen()) {
            this.connection = this.connectionFactory.newConnection();
        }
        return this.connection;
    }

    protected Channel getChannel() throws IOException, TimeoutException {
        if (this.connection == null || !this.connection.isOpen()) {
            this.getConnection();
        }
        if (this.channel == null || !this.channel.isOpen()) {
            this.channel = this.getConnection().createChannel();
            prepChannel(this.channel);
        }
        return this.channel;
    }

    /**
     * bind channel with exchange and queue
     * @param channel
     * @throws IOException
     * @throws TimeoutException
     */
    protected abstract void prepChannel(Channel channel) throws IOException, TimeoutException;

    /**
     * produce message
     * @param msg message body
     * @param routingKey if no routing key, pass ""
     * @throws IOException
     * @throws TimeoutException
     */
    public abstract void sendSimpleMsg(String msg, String routingKey) throws IOException, TimeoutException;
}
