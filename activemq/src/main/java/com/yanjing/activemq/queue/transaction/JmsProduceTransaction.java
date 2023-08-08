package com.yanjing.activemq.queue.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduceTransaction {
    private static  final String ACTIVEMQ_URL = "tcp://192.168.86.128:61616";
    private static final String QUEUE_NAME = "queue_TX";
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //开启事务需要commit
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageProducer messageProducer = session.createProducer(queue);

        try {
            for (int i = 0; i < 3; i++) {
                //设置提交异常情况
                /*if(i ==2){
                    System.out.println(i/0);
                }*/
                TextMessage textMessage = session.createTextMessage("msg---hello" + i);
                messageProducer.send(textMessage);
            }
            session.commit();
            System.out.println("事务------消息发送完成");
        }catch (Exception e) {
            System.out.println("生产者出现异常,消息回滚");
            session.rollback();
        } finally {
            //9.关闭资源
            messageProducer.close();
            session.close();
            System.out.println("****QUEUE_NAME消息发布到MQ队列完成");
        }



    }
}
