package com.yanjing.activemq.mysql;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {
    //private static  final String ACTIVEMQ_URL = "auto+nio://192.168.86.128:61608";

    //失败转移协议
    private static  final String ACTIVEMQ_URL = "failover:(auto+nio://192.168.86.128:61616,auto+nio://192.168.86.128:61617,auto+nio://192.168.86.128:61618)";
    private static final String QUEUE_NAME = "Queue-JdbcPersistence-failover";
    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂,按给定的url，采用默认的用户名密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂,获得connection并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3.创建会话session
        //两个参数transacted=事务,acknowledgeMode=确认模式(签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地(具体是队列queue还是主题topic)
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(queue);
        //5.1设置消息生产者非持久性（默认是持久的）
        /**
         * mysql一定要开启持久化功能
         */
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        //6.通过使用消息生产者,生产三条消息,发送到MQ的队列里面
        for (int i = 0; i < 3; i++) {
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("Queue-JdbcPersistence:msg---hello" + i);//理解为一个字符串
            //8.通过messageProducer发送给MQ队列
            messageProducer.send(textMessage);
        }
        //9.关闭资源
        messageProducer.close();
        session.close();
        System.out.println("****QUEUE_NAME消息发布到MQ队列完成");

    }
}
