package com.example.demo.connection;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author STEVE on 1/11/2022
 */
@Component
@PropertySource(value = "classpath:connection.properties", ignoreResourceNotFound = true)
public class RabbitConn {
    ConnectionFactory connectionFactory;
    @Value("${connection.RABBIT-HOST}")
    private String host = "localhost";
    @Value("${connection.RABBIT-PORT}")
    private int port = 5672;
    @Value("${connection.RABBIT-VHOST}")
    private String vHost = "my_vhost";
    @Value("${connection.RABBIT-USERNAME}")
    private String username = "user";
    @Value("${connection.RABBIT-PASSWORD}")
    private String password = "password";


    public ConnectionFactory getConnectionFactory() {
        if (connectionFactory == null) {
            this.connectionFactory = new ConnectionFactory();
            this.connectionFactory.setHost(host);
            this.connectionFactory.setPort(port);
            this.connectionFactory.setVirtualHost(vHost);
            this.connectionFactory.setUsername(username);
            this.connectionFactory.setPassword(password);
        }
        return this.connectionFactory;
    }

}
