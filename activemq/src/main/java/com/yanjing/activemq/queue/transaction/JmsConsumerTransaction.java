package com.yanjing.activemq.queue.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumerTransaction {
    private static final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";
    private static final String QUEUE_NAME = "queue_TX";

    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("*************我是4号消费者");  //多实例运行，开启两个客户端

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //消费者开启了事务就必须手动提交，不然会重复消费消息
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageConsumer messageConsumer = session.createConsumer(queue);

        /**
         * 1.这种方式能实现出错的消息不能提交
         */
        int a = 0;
        try {
            while (true) {

                //设置回滚异常
                if (a == 1) {
                    System.out.println(a / 0);
                }

                TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);  //4秒无消息就关闭控制作台,不再监听接收消息
                System.out.println("****消费者接收到的消息:  " + textMessage.getText());
                //session.commit(); //这种会提交正确的，后面session.rollback();也没回滚消息
                a = a + 1;
            }
        } catch (Exception e) {
            try {
                System.out.println("出现异常，消费失败，放弃消费");
                session.rollback();
                a = 0;
            } catch (JMSException ex) {
                ex.printStackTrace();
            }
        }finally {

            //写在finally，消息都正确能提交
            session.commit();

            messageConsumer.close();
            session.close();
            connection.close();
        }

        /**
         * 2.这种方式实现有问题，当a==1报错，消息也会被消费
         */
        /*messageConsumer.setMessageListener(new MessageListener() {
            int a = 0;
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage) {
                    try {
                        if (a == 1) {
                            System.out.println(a / 0);
                        }
                        TextMessage textMessage = (TextMessage) message;
                        System.out.println("***消费者接收到的消息:   " + textMessage.getText());
                        session.commit();
                        a = a + 1;
                    } catch (Exception e) {
                        try {
                            System.out.println("出现异常，消费失败，放弃消费");
                            session.rollback();
                            a=0;
                        } catch (JMSException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });*/


    }
}
