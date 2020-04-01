package com.seckill.dis.rmq.producer.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;
import com.seckill.dis.cache.facade.RedisServiceApi;
import com.seckill.dis.cache.facade.vo.GoodsKeyPrefix;
import com.seckill.dis.common.result.CodeMsg;
import com.seckill.dis.common.result.Result;
import com.seckill.dis.rmq.facade.vo.SkMessage;
import org.apache.dubbo.config.annotation.Reference;

import java.util.Date;

/**
 * @author Administrator
 * @version V1.0
 * @ClassName: My
 * @Description: TODO
 * @Date 2020/3/28 14:13
 */
public class MyLocalTransactionExecuter implements LocalTransactionExecuter {
    RedisServiceApi redisService;

    public MyLocalTransactionExecuter(RedisServiceApi redisService){
        this.redisService = redisService;
    }

    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        try {
            //DB操作 应该带上事务 service -> dao
            //如果数据操作失败  需要回滚    同事返回RocketMQ一个失败消息  意味着 消费者无法消费到这条失败的消息
            //如果成功 就要返回一个rocketMQ成功的消息，意味着消费者将读取到这条消息
            //o就是attachment
            //测试代码
            // 预减库存，同时在库存为0时标记该商品已经结束秒杀
//            String msg1 = new String(msg.getBody());
//            SkMessage skMessage = JSON.parseObject(msg1, SkMessage.class);
//            Long stock = redisService.decr(GoodsKeyPrefix.GOODS_STOCK, "" + skMessage.getGoodsId());
//            if (stock < 0) {
//                return LocalTransactionState.ROLLBACK_MESSAGE;
//            }
        } catch (Exception e) {
            System.out.println(new Date() + "===> 本地事务执行失败！！！");
            return LocalTransactionState.ROLLBACK_MESSAGE;

        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
