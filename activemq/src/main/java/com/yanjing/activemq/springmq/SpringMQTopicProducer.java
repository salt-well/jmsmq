package com.yanjing.activemq.springmq;

import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

@Service
public class SpringMQTopicProducer {
    private JmsTemplate jmsTemplate;

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate){
        this.jmsTemplate =jmsTemplate;
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml");
        SpringMQTopicProducer springMQTopicProducer = applicationContext.getBean(SpringMQTopicProducer.class);
        //直接调用application.xml里面创建的destinationTopic这个bean设置为目的地就行了
        springMQTopicProducer.jmsTemplate.setDefaultDestination((Destination)applicationContext.getBean("destinationTopic"));
        springMQTopicProducer.jmsTemplate.send(session ->session.createTextMessage("***Spring和ActiveMQ的整合topic222....."));
        System.out.println("*****生产topic消息发送完成");
    }

}
