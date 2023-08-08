package com.yanjing.activemq.topic;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class TopicConsumer {
    //需要在监听方法指定连接工厂
    @JmsListener(destination = "${myTopicName}",containerFactory = "jmsListenerContainerFactory")
    public void consumer(TextMessage textMessage) throws JMSException {
        System.out.println("springboot_topic_persist订阅着收到消息:    " + textMessage.getText());
    }
}