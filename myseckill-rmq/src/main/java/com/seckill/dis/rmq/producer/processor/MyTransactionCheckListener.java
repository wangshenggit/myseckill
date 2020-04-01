package com.seckill.dis.rmq.producer;

import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @author Administrator
 * @version V1.0
 * @ClassName: MyTransactionCheckListener
 * @Description: TODO
 * @Date 2020/3/28 13:43
 */
public class MyTransactionCheckListener implements TransactionCheckListener {
    @Override
    public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
        System.out.println("服务器端回查事务消息： " + msg.toString());
        //由于RocketMQ迟迟没有收到消息的确认消息，因此主动询问这条prepare消息，是否正常？
        //可以查询数据库看这条数据是否已经处理

        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
