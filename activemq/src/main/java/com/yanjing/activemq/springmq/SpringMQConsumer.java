package com.yanjing.activemq.springmq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SpringMQConsumer {


    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("Application.xml");
        SpringMQConsumer springMQ_consumer = applicationContext.getBean(SpringMQConsumer.class);
        String returnValue = (String) springMQ_consumer.jmsTemplate.receiveAndConvert();
        System.out.println("****消费者收到的消息:   " + returnValue);
    }
}
