package com.yanjing.boot.activemq;

import com.yanjing.boot.activemq.queue.QueueProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = MainMQSpringbootProducer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class TestActiveMQ {
    @Autowired
    private QueueProducer queueProducer;

    @Test
    public void testSend() {
        queueProducer.producerMessage();
    }
}
