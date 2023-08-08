package com.yanjing.activemq.mysql;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopic {

        private static final String ACTIVEMQ_URL = "auto+nio://192.168.86.128:61608";
        private static final String ACTIVEMQ_TOPIC_NAME = "Topic-JdbcPersistence";

        public static void main(String[] args) throws JMSException, IOException {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            activeMQConnectionFactory.setBrokerURL(ACTIVEMQ_URL);
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.setClientID("我是消费者李四");

            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(ACTIVEMQ_TOPIC_NAME);
            TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "我是消费者李四我要订阅这个消息");

            connection.start();

            topicSubscriber.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println("消费者李四收到的消息： " + textMessage.getText());
                            session.commit();
                        } catch (JMSException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            System.in.read();
        }



}
