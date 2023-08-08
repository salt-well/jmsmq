package com.yanjing.activemq.topic;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class TopicConsumer {

    @JmsListener(destination = "${myTopicName}")
    public void consumer(TextMessage textMessage) throws JMSException {
        System.out.println("springboot_topic_no_persist订阅着收到消息:    " + textMessage.getText());
    }
}
