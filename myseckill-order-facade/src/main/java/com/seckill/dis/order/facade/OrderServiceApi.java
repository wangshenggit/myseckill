package com.seckill.dis.order.facade;

import com.seckill.dis.common.domain.OrderInfo;
import com.seckill.dis.common.domain.SeckillOrder;
import com.seckill.dis.goods.facade.vo.GoodsVo;
import com.seckill.dis.user.facade.vo.UserVo;

/**
 * 订单服务接口
 *
 * @author noodle
 */
public interface OrderServiceApi {
    /**
     * 通过订单id获取订单
     *
     * @param orderId
     * @return
     */
    OrderInfo getOrderById(long orderId);

    /**
     * 通过用户id与商品id从订单列表中获取订单信息，这个地方用到了唯一索引（unique index!!!!!）
     *
     * @param userId
     * @param goodsId
     * @return 秒杀订单信息
     */
    SeckillOrder getSeckillOrderByUserIdAndGoodsId(long userId, long goodsId);

    /**
     * 创建订单
     *
     * @param user
     * @param goods
     * @return
     */
    OrderInfo createOrder(UserVo user, GoodsVo goods);
}
