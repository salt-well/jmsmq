package com.yanjing.activemq.broker;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.Connection;

/**
 * 使用Broker,相当于一个mini版本的本地实例actiemq
 * 先启动此broker
 * 使用命令 ./activemq start xbean:file:/usr/local/activemq/apache-activemq-5.15.5/conf/activemq02.xml启动Broker 启动多个activemq
 */
public class EmbedBroker {
    public static void main(String[] args) throws Exception {
        //ActiveMQ也支持在vm中通信基于嵌入的broker
        BrokerService brokerService = new BrokerService();
        brokerService.setPopulateJMSXUserID(true);
        brokerService.addConnector("tcp://127.0.0.1:61616");
        brokerService.start();
    }
}
