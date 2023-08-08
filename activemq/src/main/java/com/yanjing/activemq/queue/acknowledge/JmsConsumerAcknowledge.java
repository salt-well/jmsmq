package com.yanjing.activemq.queue.acknowledge;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumerAcknowledge {
    private static  final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";

    //private static final String ACTIVEMQ_QUEUE_NAME = "Queue-ACK-NoTransaction";

    private static final String ACTIVEMQ_QUEUE_NAME = "Queue-ACK-Transaction";

    /**
     * 1.没有事务签收
     * 需要手动签收textMessage.acknowledge();
     */
    /*public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //消费者设置了手动签收,就必须自己签收,向服务器发送我已经收到消息了
        //开启事务如果不提交,就算手动签收,也是无效的
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        MessageConsumer messageConsumer = session.createConsumer(queue);

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        //手动签收
                        textMessage.acknowledge();
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }*/

    /**
     * 2.有事务签收
     * 不用手动签收textMessage.acknowledge();事务更大，执行session.commit()会自动执行签收
     */
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //消费者设置了手动签收,就必须自己签收,向服务器发送我已经收到消息了
        //开启事务如果不提交,就算手动签收,也是无效的
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        MessageConsumer messageConsumer = session.createConsumer(queue);

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        //手动签收
                        //textMessage.acknowledge();
                        //提交事务
                        session.commit();
                        System.out.println(textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
