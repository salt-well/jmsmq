package com.yanjing.activemq.asyncsends;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class Jms_TX_Consumer {

        //private static  final String ACTIVEMQ_URL = "auto+nio://192.168.86.128:61608";

        //失败转移协议
        private static  final String ACTIVEMQ_URL = "failover:(auto+nio://192.168.86.128:61616,auto+nio://192.168.86.128:61617,auto+nio://192.168.86.128:61618)";
        private static final String ACTIVEMQ_QUEUE_NAME = "async-queue";

        public static void main(String[] args) throws JMSException, IOException {
            //System.out.println("*************我是1号消费者");  //多实例运行，开启两个客户端
            System.out.println("*************我是Queue-Async消费者");

            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
            Connection connection = activeMQConnectionFactory.createConnection();


            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
            MessageConsumer messageConsumer = session.createConsumer(queue);

            connection.start();

            messageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    if (message != null && message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        try {
                            System.out.println("****Queue-Async消费者接收到的消息:  " + textMessage.getText());
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
