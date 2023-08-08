package com.yanjing.activemq.dead;

import javax.jms.*;
import java.io.IOException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

public class JmsDeadConumer {
    private static final String ACTIVEMQ_URL = "failover:(auto+nio://192.168.86.128:61616,auto+nio://192.168.86.128:61617,auto+nio://192.168.86.128:61618)";
    private static final String ACTIVEMQ_QUEUE_NAME = "dead-queue";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*************我是dead消费者");

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 修改默认参数(默认重试6次)，设置消息消费重试3次(客户端连续三次没接收到就变成为死信队列)
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setMaximumRedeliveries(3);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();


        //Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //开启事务
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        MessageConsumer messageConsumer = session.createConsumer(queue);

        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("****dead消费者接收到的消息:  " + textMessage.getText());
                        //开启事务没提交，测试死信队列
                        //session.commit();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //保证控制台不关闭,阻止程序关闭
        System.in.read();

        //8.关闭资源
        messageConsumer.close();
        session.close();
        connection.close();


    }
}
