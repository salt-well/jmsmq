package com.yanjing.activemq.topic.persist;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTopicPersist {
    private static final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";
    private static final String TOPIC_NAME_PERSIST = "Topic_Persist";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*************我是3号持久化订阅者");
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        //设置订阅者ID
        connection.setClientID("小罗");


        Session session =connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(TOPIC_NAME_PERSIST);
        //持久化订阅不需要messageConsumer
        //MessageConsumer messageConsumer = session.createConsumer(topic);

        //通过session创建持久化订阅(queue消费都不用写，topic要写)
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "我是小罗，足球");
        connection.start();

        topicSubscriber.setMessageListener(message-> {
            if (message != null && message instanceof TextMessage){
                try {
                    TextMessage textMessage = (TextMessage) message;
                    System.out.println("收到的持久化订阅消息: " + textMessage.getText());
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //保证控制台不关闭,阻止程序关闭
        System.in.read();
        //messageConsumer.close();
        session.close();
        connection.close();
    }

    /**
     * 一定要先运行一次消费者,类似于像MQ注册,我订阅了这个主题
     * 然后再运行主题生产者
     * 无论消费着是否在线,都会接收到,在线的立即接收到,不在线的等下次上线把没接收到的接收
     */

}
