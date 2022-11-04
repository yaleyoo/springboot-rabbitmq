package com.example.demo.formBean;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author STEVE on 25/10/2022
 */
@Data
public class MessageBean {
    private String topic;
    private String message;
}
