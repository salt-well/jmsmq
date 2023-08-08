package com.yanjing.activemq.config;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

@Component
@EnableJms
public class ActiveMQConfigBean {
    @Value("${myTopicName}")
    private String topicName;

    @Bean
    public ActiveMQTopic activeMQTopic() {
        return new ActiveMQTopic(topicName);
    }
}
