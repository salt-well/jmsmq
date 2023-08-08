package com.yanjing.activemq.asyncsends;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.AsyncCallback;

import javax.jms.*;
import java.util.UUID;

/**
 * 异步投递
 * 2.(3)异步发送如何确认发送成功
 */
public class Jms_TX_Producer {

    // 方式1。3种方式任选一种
    //private static final String ACTIVEMQ_URL = "tcp://118.24.20.3:61626?jms.useAsyncSend=true";
    private static  final String ACTIVEMQ_URL = "failover:(auto+nio://192.168.86.128:61616,auto+nio://192.168.86.128:61617,auto+nio://192.168.86.128:61618)";
    private static final String ACTIVEMQ_QUEUE_NAME = "async-queue";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        // 方式2
        activeMQConnectionFactory.setUseAsyncSend(true);
        Connection connection = activeMQConnectionFactory.createConnection();
        // 方式3
        //((ActiveMQConnection)connection).setUseAsyncSend(true);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        //MessageProducer producer = session.createProducer(queue);  //（1）原：
        //（2）新：异步增加确认投递
        ActiveMQMessageProducer activeMQMessageProducer = (ActiveMQMessageProducer)session.createProducer(queue);
        try {
            for (int i = 0; i < 3; i++) {
                TextMessage textMessage = session.createTextMessage("tx msg--" + i);
                //producer.send(textMessage);  //(1)
                //(2)异步投递确认消息是否到达
                textMessage.setJMSMessageID(UUID.randomUUID().toString()+"orderYanjing");
                final String  msgId = textMessage.getJMSMessageID();
                activeMQMessageProducer.send(textMessage, new AsyncCallback() {
                    public void onSuccess() {
                        System.out.println("async成功发送消息Id:"+msgId);
                    }

                    public void onException(JMSException e) {
                        System.out.println("async失败发送消息Id:"+msgId);
                    }
                });
            }
            System.out.println("消息发送完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //producer.close();
            activeMQMessageProducer.close();
            session.close();
            connection.close();
        }
    }
}
