package com.example.demo.controllers;

import com.example.demo.formBean.MessageBean;
import com.example.demo.rabbitmq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author STEVE on 1/11/2022
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    @Autowired
    private WorkerProducer workerProducer;
    @Autowired
    private BroadCastProducer broadCastProducer;
    @Autowired
    private RoutingProducer routingProducer;
    @Autowired
    private TopicProducer topicProducer;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public ResponseEntity<?> sendRabbitMsg(@RequestBody MessageBean msg) throws IOException, TimeoutException {
        workerProducer.sendSimpleMsg(msg.getMessage(), "");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/fireTenMsg", method = RequestMethod.POST)
    public ResponseEntity<?> sendTenRabbitMsg() throws IOException, TimeoutException {
        for (int i=0; i<10; i++)
            workerProducer.sendSimpleMsg("This is msg no." + i, "");

        return new ResponseEntity<>("worker done", HttpStatus.OK);
    }

    @RequestMapping(value = "/broadCastTenMsg", method = RequestMethod.POST)
    public ResponseEntity<?> broadCastTenRabbitMsg() throws IOException, TimeoutException {
        for (int i=0; i<10; i++)
            broadCastProducer.sendSimpleMsg("This is msg no." + i, "");

        return new ResponseEntity<>("broadcast done", HttpStatus.OK);
    }

    @RequestMapping(value = "/routingTenMsg", method = RequestMethod.POST)
    public ResponseEntity<?> routingTenRabbitMsg() throws IOException, TimeoutException {
        for (int i=0; i<10; i++) {
            int seed = (int) Math.floor(Math.random() * 100);

            routingProducer.sendSimpleMsg("This is msg no." + i + " with seed: " + seed,
                    RoutingProducer.routingKeys[seed%RoutingProducer.routingKeys.length]);
        }

        return new ResponseEntity<>("broadcast done", HttpStatus.OK);
    }

    @RequestMapping(value = "/sendTopicMsg", method = RequestMethod.POST)
    public ResponseEntity<?> topicRabbitMsg(@RequestBody MessageBean msg) throws IOException, TimeoutException {
        topicProducer.sendSimpleMsg(msg.getMessage(), msg.getTopic());

        return new ResponseEntity<>("topic send done", HttpStatus.OK);
    }
}
