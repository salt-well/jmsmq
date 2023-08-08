package com.yanjing.activemq.topic;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@EnableScheduling
public class TopicProducer {
    private JmsMessagingTemplate jmsMessagingTemplate;
    private ActiveMQTopic activeMQTopic;

    @Scheduled(fixedDelay = 3000)
    public void producer() {
        jmsMessagingTemplate.convertAndSend(activeMQTopic, "主题消息:    " + UUID.randomUUID().toString());
    }

    public TopicProducer(JmsMessagingTemplate jmsMessagingTemplate, ActiveMQTopic activeMQTopic) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.activeMQTopic = activeMQTopic;
    }
}
