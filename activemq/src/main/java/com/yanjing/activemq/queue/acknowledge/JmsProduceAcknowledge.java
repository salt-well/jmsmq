package com.yanjing.activemq.queue.acknowledge;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduceAcknowledge {
    private static  final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";

    //private static final String ACTIVEMQ_QUEUE_NAME = "Queue-ACK-NoTransaction";

    private static final String ACTIVEMQ_QUEUE_NAME = "Queue-ACK-Transaction";

    /**
     * 1.没有事务签收
     */
    /*public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        MessageProducer producer = session.createProducer(queue);

        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("ACK-msg:   " + i);
            producer.send(textMessage);
        }
        System.out.println("ACK-msg发送完成");

        producer.close();
        session.close();
        connection.close();
    }*/

    /**
     * 2.有事务签收
     */
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        MessageProducer producer = session.createProducer(queue);

        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("ACK-Transaction-msg:   " + i);
            producer.send(textMessage);
        }
        session.commit();
        System.out.println("ACK-Transaction-msg发送完成");

        producer.close();
        session.close();
        connection.close();
    }
}
