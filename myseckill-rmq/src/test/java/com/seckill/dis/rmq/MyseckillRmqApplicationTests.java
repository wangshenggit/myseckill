package com.seckill.dis.rmq;


import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.seckill.dis.rmq.constants.TagConstants;
import com.seckill.dis.rmq.constants.TopicEnum;
import com.seckill.dis.rmq.producer.processor.MyLocalTransactionExecuter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MyseckillRmqApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(MyseckillRmqApplicationTests.class);


    @Autowired
    private TransactionMQProducer transactionMQProducer;

    /**
     * 发送消息
     *
     * 2018年3月3日 zhaowg
     * @throws InterruptedException
     * @throws MQBrokerException
     * @throws RemotingException
     * @throws MQClientException
     */
    @Test
    public void sendWithTransactionMQProducer() throws MQClientException, RemotingException, MQBrokerException, InterruptedException{
//        String msg = "demo msg test111111111";
//        logger.info("生产者开始发送消息："+msg);
//        Message sendMsg = new Message(TopicEnum.DemoTopic.getCode(), TagConstants.DemoTopic.DemoTag,msg.getBytes());
//        //默认3秒超时
//        //SendResult sendResult = defaultMQProducer.send(sendMsg);
//
//        SendResult sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, new MyLocalTransactionExecuter(), null);
//        logger.info("消息发送响应信息："+sendResult.toString());
    }

}
