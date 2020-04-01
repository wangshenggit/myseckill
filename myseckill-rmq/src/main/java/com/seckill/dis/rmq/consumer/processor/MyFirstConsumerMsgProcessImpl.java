package com.seckill.dis.rmq.producer.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.seckill.dis.common.api.cache.RedisServiceApi;
import com.seckill.dis.common.api.cache.vo.OrderKeyPrefix;
import com.seckill.dis.common.api.goods.GoodsServiceApi;
import com.seckill.dis.common.api.goods.vo.GoodsVo;
import com.seckill.dis.common.api.mq.vo.SkMessage;
import com.seckill.dis.common.api.order.OrderServiceApi;
import com.seckill.dis.common.api.seckill.SeckillServiceApi;
import com.seckill.dis.common.api.user.vo.UserVo;
import com.seckill.dis.common.domain.SeckillOrder;
import com.seckill.dis.rmq.annotation.MQConsumeService;
import com.seckill.dis.rmq.constants.TopicEnum;
import com.seckill.dis.rmq.consumer.processor.AbstractMQMsgProcessor;
import com.seckill.dis.rmq.consumer.processor.MQConsumeResult;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;

@MQConsumeService(topic = TopicEnum.DemoTopic, tags = {"*"})
public class MyFirstConsumerMsgProcessImpl extends AbstractMQMsgProcessor {

    @Reference(interfaceClass = GoodsServiceApi.class)
    GoodsServiceApi goodsService;

    @Reference(interfaceClass = OrderServiceApi.class)
    OrderServiceApi orderService;

    @Reference(interfaceClass = SeckillServiceApi.class)
    SeckillServiceApi seckillService;

    @Reference(interfaceClass = RedisServiceApi.class)
    RedisServiceApi redisService;


    @Override
    protected MQConsumeResult consumeMessage(String tag, List<String> keys, MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        logger.info("消费者获取到的消息为：" + msg);

        //TODO 判断该消息是否重复消费（RocketMQ不保证消息不重复，如果你的业务需要保证严格的不重复消息，需要你自己在业务端去重）
        //如果注解中tags数据中包含多个tag或者是全部的tag(*)，则需要根据tag判断是那个业务，
        //如果注解中tags为具体的某个tag，则该服务就是单独针对tag处理的
        if (tag.equals("某个tag")) {
            //做某个操作
        }
        //TODO 获取该消息重试次数
        int reconsume = messageExt.getReconsumeTimes();
        //根据消息重试次数判断是否需要继续消费
        if (reconsume == 3) {//消息已经重试了3次，如果不需要再次消费，则返回成功

        }

        String str="SeckillMessage{user=UserVo{uuid=3, phone=15710647236, nickname='123', password='b7797cce01b4b131b433b6acf4add449', salt='1a2b3c4d', head='123', registerDate=Sun Mar 29 13:57:04 CST 2020, lastLoginDate=null, loginCount=null}, goodsId=1}";

        SkMessage skMessage = JSON.parseObject(msg, SkMessage.class);
        // 获取秒杀用户信息与商品id
        UserVo user = skMessage.getUser();
        long goodsId = skMessage.getGoodsId();

        MQConsumeResult result = new MQConsumeResult();
        // 获取商品的库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        Integer stockCount = goods.getStockCount();
        if (stockCount <= 0) {
            result.setSuccess(false);
            return result;
        }

        // 判断是否已经秒杀到了（保证秒杀接口幂等性）
        SeckillOrder order = this.getSkOrderByUserIdAndGoodsId(user.getUuid(), goodsId);
        if (order != null) {
            result.setSuccess(false);
            return result;
        }

        // 1.减库存 2.写入订单 3.写入秒杀订单
        seckillService.seckill(user, goods);
        result.setSuccess(true);
        return result;
    }

    /**
     * 通过用户id与商品id从订单列表中获取订单信息，这个地方用了唯一索引（unique index!!!!!）
     * <p>
     * 优化，不同每次都去数据库中读取秒杀订单信息，而是在第一次生成秒杀订单成功后，
     * 将订单存储在redis中，再次读取订单信息的时候就直接从redis中读取
     *
     * @param userId
     * @param goodsId
     * @return 秒杀订单信息
     */
    private SeckillOrder getSkOrderByUserIdAndGoodsId(Long userId, long goodsId) {

        // 从redis中取缓存，减少数据库的访问
        SeckillOrder seckillOrder = redisService.get(OrderKeyPrefix.SK_ORDER, ":" + userId + "_" + goodsId, SeckillOrder.class);
        if (seckillOrder != null) {
            return seckillOrder;
        }
        return orderService.getSeckillOrderByUserIdAndGoodsId(userId, goodsId);
    }


}
