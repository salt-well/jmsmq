package com.yanjing.boot.activemq.queue;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.TextMessage;

@Component
public class QueueConsumer{
    /**
     * 监听接收的方法
     * 监听会随着springboot一起启动，有了消息就执行加了@JmsListener注解的监听方法
     */
    @JmsListener(destination = "${myQueueName}")
    public void consumerMsg(TextMessage textMessage) throws JMSException {
        String text = textMessage.getText();
        System.out.println("***springbootMQ消费者收到的消息:    " + text);
    }
}
