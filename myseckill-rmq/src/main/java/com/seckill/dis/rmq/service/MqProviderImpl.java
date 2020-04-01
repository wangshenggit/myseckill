package com.seckill.dis.rmq.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.seckill.dis.cache.facade.RedisServiceApi;
import com.seckill.dis.rmq.constants.TagConstants;
import com.seckill.dis.rmq.constants.TopicEnum;
import com.seckill.dis.rmq.facade.MqProviderApi;
import com.seckill.dis.rmq.facade.vo.SkMessage;
import com.seckill.dis.rmq.producer.processor.MyLocalTransactionExecuter;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.UUID;


/**
 * 消息队列服务化（消息生产者）
 *
 * @author noodle
 */

@Service(interfaceClass = MqProviderApi.class)
public class MqProviderImpl implements MqProviderApi {

    private static Logger logger = LoggerFactory.getLogger(MqProviderImpl.class);

    @Autowired
    private TransactionMQProducer transactionMQProducer;

    @Reference(interfaceClass = RedisServiceApi.class)
    RedisServiceApi redisService;


    @Override
    public void sendSkMessage(SkMessage message) throws Exception {
        logger.info("RMQ send message: " + message);

        logger.info("开始发送消息：" + message.toString());
        Message sendMsg = new Message(TopicEnum.DemoTopic.getCode(), TagConstants.DemoTopic.DemoTag, JSON.toJSONString(message).getBytes());

        SendResult sendResult = transactionMQProducer.sendMessageInTransaction(sendMsg, new MyLocalTransactionExecuter(redisService), null);

        logger.info("消息发送响应信息：" + sendResult.toString());

    }


}
