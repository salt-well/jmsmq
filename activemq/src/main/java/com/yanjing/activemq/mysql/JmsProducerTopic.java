package com.yanjing.activemq.mysql;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProducerTopic {

        private static final String ACTIVEMQ_URL = "auto+nio://192.168.86.128:61608";
        private static final String ACTIVEMQ_TOPIC_NAME = "Topic-JdbcPersistence";

        public static void main(String[] args) throws JMSException {
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
            activeMQConnectionFactory.setBrokerURL(ACTIVEMQ_URL);
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.setClientID("我是生产者张三");

            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(ACTIVEMQ_TOPIC_NAME);
            MessageProducer messageProducer = session.createProducer(topic);
            messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);

            connection.start();
            for (int i = 0; i < 3; i++) {
                TextMessage textMessage = session.createTextMessage("Topic-JdbcPersistence测试消息" + i);
                messageProducer.send(textMessage);
            }
            session.commit();

            System.out.println("主题发送到MQ完成");

            messageProducer.close();
            session.close();
            connection.close();
        }


}
