package com.yanjing.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopic {
    private static final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";
    private static final String TOPIC_NAME = "topic01";

    public static void main(String[] args) throws JMSException, IOException {
        //System.out.println("*************我是1号订阅者");
        System.out.println("*************我是2号订阅者");
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session =connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageConsumer messageConsumer = session.createConsumer(topic);

        /*1.
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage){
                    String text = null;
                    try {
                        text = ((TextMessage) message).getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });*/

        //2.lamada表达式
        messageConsumer.setMessageListener(message-> {
                if (message != null && message instanceof TextMessage){
                    try {
                        String text = ((TextMessage) message).getText();
                        System.out.println(text);
                    } catch (JMSException e) {
                        throw new RuntimeException(e);
                    }
                }
        });

        //保证控制台不关闭,阻止程序关闭
        System.in.read();
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
