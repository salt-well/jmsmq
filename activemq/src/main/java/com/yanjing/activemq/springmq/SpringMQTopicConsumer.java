package com.yanjing.activemq.springmq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

@Service
public class SpringMQTopicConsumer {
    private JmsTemplate jmsTemplate;

    @Autowired
    private void setJmsTemplate(JmsTemplate jmsTemplate){
        this.jmsTemplate = jmsTemplate;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext =new ClassPathXmlApplicationContext("application.xml");
        SpringMQTopicConsumer springMQTopicConsumer = applicationContext.getBean(SpringMQTopicConsumer.class);
        //直接调用application.xml里面创建的destinationTopic这个bean设置为目的地就行了
        springMQTopicConsumer.jmsTemplate.setDefaultDestination(((Destination)applicationContext.getBean("destinationTopic")));
        String returnValue = (String)springMQTopicConsumer.jmsTemplate.receiveAndConvert();
        System.out.println("****消费者收到的消息:   " + returnValue);
    }
}
