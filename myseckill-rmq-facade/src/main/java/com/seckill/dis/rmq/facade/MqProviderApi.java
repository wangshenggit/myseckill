package com.seckill.dis.rmq.facade;


import com.seckill.dis.rmq.facade.vo.SkMessage;

/**
 * 消息队列服务
 *
 * @author noodle
 */
public interface MqProviderApi {

    /**
     * 将用户秒杀信息投递到MQ中（使用direct模式的exchange）
     *
     * @param message
     */
    void sendSkMessage(SkMessage message) throws Exception;
}
