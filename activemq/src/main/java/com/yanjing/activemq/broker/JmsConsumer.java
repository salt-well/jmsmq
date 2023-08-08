package com.yanjing.activemq.broker;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer {
    private static  final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    private static final String QUEUE_NAME = "broker_queue";

    public static void main(String[] args) throws JMSException, IOException {
        //System.out.println("*************我是1号消费者");  //多实例运行，开启两个客户端
        System.out.println("*************我是2号消费者");

        //1.创建连接工厂，按照给定的URL，采用默认的用户密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获取connection 并启动访问
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //3.创建会话session
        //两个参数transacted=事务,acknowledgeMode=确认模式(签收)
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.创建目的地（具体是队列还topic主题）
        Queue queue = session.createQueue(QUEUE_NAME);
        //5.创建消费者，指定消费哪一个队列里面的消息
        MessageConsumer messageConsumer = session.createConsumer(queue);
        /*循环获取
          1.同步阻塞式
         */
        /*while (true){
            //6.通过消费者调用方法获取队里面的消息(发送的是什么类型，接收的时候就强转成什么类型)
            TextMessage textMessage = (TextMessage) messageConsumer.receive();
            if (textMessage != null){
                System.out.println("****消费者接收到的消息:  "  + textMessage.getText());
            }else {
                break;
            }
        }*/

        /*
        2.异步非阻塞式方式监听器(onMessage)
        订阅者或消费者通过创建的消费者对象,给消费者注册消息监听器setMessageListener,
        当消息有消息的时候,系统会自动调用MessageListener类的onMessage方法
        我们只需要在onMessage方法内判断消息类型即可获取消息
         */
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message != null && message instanceof TextMessage) {
                    //7.把message转换成消息发送前的类型并获取消息内容
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("****消费者接收到的消息:  " + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.out.println("执行了39行");
        //保证控制台不关闭,阻止程序关闭
        System.in.read();


        //8.关闭资源
        messageConsumer.close();
        session.close();
        connection.close();


    }
}
