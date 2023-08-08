package com.yanjing.activemq.asyncsends;

import org.apache.activemq.*;

import javax.jms.*;
import java.util.UUID;

/**
 * 延时投递和定时投递
 *
 */
public class JmsDelayAndPeriodProducer {


    private static  final String ACTIVEMQ_URL = "failover:(auto+nio://192.168.86.128:61616,auto+nio://192.168.86.128:61617,auto+nio://192.168.86.128:61618)";
    private static final String ACTIVEMQ_QUEUE_NAME = "delay-period-queue";

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(ACTIVEMQ_QUEUE_NAME);
        MessageProducer producer = session.createProducer(queue);

        long delay =  10*1000;
        long period = 5*1000;
        int repeat = 3 ;

        try {
            for (int i = 0; i < 3; i++) {
                TextMessage textMessage = session.createTextMessage("tx msg--" + i);
                // 延迟的时间
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
                // 重复投递的时间间隔
                textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
                // 重复投递的次数
                textMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat-1);
                // 此处的意思：该条消息，等待10秒，之后每5秒发送一次，重复发送3次。
                producer.send(textMessage);

            }
            System.out.println("delay-period消息发送完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
            session.close();
            connection.close();
        }
    }
}
