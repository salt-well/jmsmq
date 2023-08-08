package com.yanjing.activemq.topic.persist;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProducerTopicPersist {
    private static final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";
    private static final String TOPIC_NAME_PERSIST = "Topic_Persist";

    public static void main(String[] ars) throws JMSException {
        //1.创建连接工厂，按给定的URL，使用默认的用户密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        //3.创建会话session
        //两个参数:transacted=事务,actknowledge=确认模式(签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地(具体是队列queue,还是主题topic)
        Topic topic = session.createTopic(TOPIC_NAME_PERSIST);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);

        //5.1创建生产者持久化
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //5.2启动连接，topic持久化connection.start()放在这里启动
        connection.start();

        //6.通过使用消息生产者，生产三条消息，发送到MQ的主题里面
        for (int i = 0; i < 3; i++) {
            //7.通过session创建消息
            TextMessage textMessage = session.createTextMessage("Topic_Persist-----" + i);
            //8.使用指定好目的地的消息生产都发送消息
            messageProducer.send(textMessage);
        }
        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("****Topic_Persist消息发布到MQ完成");
    }
}
