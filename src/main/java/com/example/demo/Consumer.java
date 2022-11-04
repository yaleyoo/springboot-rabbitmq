package com.example.demo;

import com.example.demo.connection.RabbitConn;
import com.example.demo.rabbitmq.TopicProducer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author STEVE on 2/11/2022
 */
public class Consumer {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Constructor<?> c = Class.forName("com.example.demo.rabbitmq.RabbitConsumer").getConstructor(RabbitConn.class, String.class);
        RabbitConn conn = new RabbitConn();

        for (String k : TopicProducer.topics)
            c.newInstance(conn, k+TopicProducer.queueSuffix);
    }
}
