package com.seckill.dis.goods.service;


import com.seckill.dis.cache.facade.RedisServiceApi;
import com.seckill.dis.cache.facade.vo.GoodsKeyPrefix;
import com.seckill.dis.common.domain.Goods;
import com.seckill.dis.common.domain.SeckillGoods;
import com.seckill.dis.goods.facade.GoodsServiceApi;
import com.seckill.dis.goods.facade.vo.GoodsVo;
import com.seckill.dis.goods.persistence.GoodsMapper;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 商品服务接口实现
 *
 * @author noodle
 */
@Service(interfaceClass = GoodsServiceApi.class)
public class GoodsServiceImpl implements GoodsServiceApi {

    @Autowired
    GoodsMapper goodsMapper;

    @Reference(interfaceClass = RedisServiceApi.class)
    RedisServiceApi redisService;

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 通过商品的id查出商品的所有信息（包含该商品的秒杀信息）
     *
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }


    /**
     * 减库存
     *
     * @param goods
     * @return
     */
    @Override
    public boolean reduceStock(GoodsVo goods) {
        SeckillGoods seckillGoods = new SeckillGoods();
        // 秒杀商品的id和商品的id是一样的
        seckillGoods.setGoodsId(goods.getId());
        int ret = goodsMapper.reduceStack(seckillGoods);
        return ret > 0;
    }

    @Override
    public boolean doTakeOff(GoodsVo goods) {
        //商品库存
        Goods goods1 = goodsMapper.getGoodsByGoodsId(goods.getId());
        //秒杀商品库存
        SeckillGoods seckillGoods = goodsMapper.getSeckillGoodsByGoodsId(goods.getId());

        if (goods1 != null && seckillGoods != null) {
            Goods goods2 = new Goods();
            goods2.setId(goods.getId());
            goods2.setGoodsStock(goods1.getGoodsStock() + seckillGoods.getStockCount());
            int result = goodsMapper.updateGoods(goods2);

            SeckillGoods seckillGoods1 = new SeckillGoods();
            seckillGoods1.setGoodsId(goods.getId());
            seckillGoods1.setStockCount(0);
            int result1 = goodsMapper.updateSeckillGoods(seckillGoods1);

            if (result > 0 && result1 > 0) {
                redisService.set(GoodsKeyPrefix.GOODS_STOCK, "" + goods.getId(), 0);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doShelves(GoodsVo goods) {
        //商品库存
        Goods goods1 = goodsMapper.getGoodsByGoodsId(goods.getId());
        //秒杀商品库存
        SeckillGoods seckillGoods = goodsMapper.getSeckillGoodsByGoodsId(goods.getId());

        if (goods1 != null && seckillGoods != null) {
            Goods goods2 = new Goods();
            goods2.setId(goods.getId());
            goods2.setGoodsStock(goods1.getGoodsStock() -10);
            int result = goodsMapper.updateGoods(goods2);

            SeckillGoods seckillGoods1 = new SeckillGoods();
            seckillGoods1.setGoodsId(goods.getId());
            seckillGoods1.setStockCount(10);
            int result1 = goodsMapper.updateSeckillGoods(seckillGoods1);

            if (result > 0 && result1 > 0) {
                redisService.set(GoodsKeyPrefix.GOODS_STOCK, "" + goods.getId(), 10);
                return true;
            }
        }
        return false;
    }
}
