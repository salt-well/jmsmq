package com.yanjing.boot.activemq.queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QueueProducer {
    private JmsMessagingTemplate jmsMessagingTemplate;

    private ActiveMQQueue activeMQQueue;

    public void producerMessage(){
        jmsMessagingTemplate.convertAndSend(activeMQQueue,"**************" + UUID.randomUUID().toString());
        System.out.println("springbootMQ  生产者发送消息成功!");
    }

    public QueueProducer(JmsMessagingTemplate jmsMessagingTemplate,ActiveMQQueue activeMQQueue){
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.activeMQQueue = activeMQQueue;
    }
}
