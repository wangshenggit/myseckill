package com.seckill.dis.rmq.producer;

import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;

import java.util.Date;

/**
 * @author Administrator
 * @version V1.0
 * @ClassName: My
 * @Description: TODO
 * @Date 2020/3/28 14:13
 */
public class MyLocalTransactionExecuter implements LocalTransactionExecuter {
    @Override
    public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
        try {
            //DB操作 应该带上事务 service -> dao
            //如果数据操作失败  需要回滚    同事返回RocketMQ一个失败消息  意味着 消费者无法消费到这条失败的消息
            //如果成功 就要返回一个rocketMQ成功的消息，意味着消费者将读取到这条消息
            //o就是attachment

            //测试代码
            System.out.println(new Date() + "===> 本地事务执行成功，发送确认消息");
        } catch (Exception e) {
            System.out.println(new Date() + "===> 本地事务执行失败！！！");
            return LocalTransactionState.ROLLBACK_MESSAGE;

        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}
