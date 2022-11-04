package com.example.demo.controllers;

import com.example.demo.formBean.MessageBean;
import com.example.demo.services.MainService;
import com.example.demo.sqs.SQSMsgConsumer;
import com.example.demo.sqs.SQSMsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author STEVE on 24/10/2022
 */
@RestController
public class MainController {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MainService mainService;

    @Autowired
    private SQSMsgSender sqsMsgSender;

    @Autowired
    private SQSMsgConsumer sqsMsgConsumer;

    @RequestMapping(value="/", method= RequestMethod.GET)
    ResponseEntity<?> all() {
        System.out.println(sqsMsgSender.hashCode());
        mainService.add("1", "2");

        return new ResponseEntity<>("hhh", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    ResponseEntity<?> sendMsg(@RequestBody MessageBean message) {
        sqsMsgSender.sendMessage(message.getMessage());

        return new ResponseEntity<>("SENT", HttpStatus.OK);
    }

    @RequestMapping(value = "/consumeMsg", method = RequestMethod.GET)
    ResponseEntity<?> consumeMsg() {
        System.out.println("consumer hash = "+sqsMsgConsumer.hashCode());
        sqsMsgConsumer.consumeMsg();

        return new ResponseEntity<>("CONSUMED", HttpStatus.OK);
    }

    @RequestMapping(value = "/fireTenMsg", method = RequestMethod.POST)
    ResponseEntity<?> fireTenMsg() {
        for (int i=0; i<10; i++) {
            sqsMsgSender.sendMessage("This is mesage No." + i);
        }

        return new ResponseEntity<>("SENT", HttpStatus.OK);
    }
}
