package com.morefun.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.morefun.common.constant.RedisKeyConstant;
import com.morefun.domain.Price;
import com.morefun.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Song
 * @date 2022/3/10 16:57
 * @Version 1.0
 */
@Slf4j
@Service
public class PriceServiceImpl implements PriceService {
    @Qualifier("objRedisTemplate")
    @Autowired
    private RedisTemplate<String, ? extends Object> redisTemplate;

    @Qualifier("strRedisTemplate")
    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;


    // 奖品1 数量
    int priceCount1 = 20;
    // 奖品2 数量
    int priceCount2 = 50;
    // 保底奖 数量
    int comfortCount = 100;


    @Override
    public Price doDraw() {
        ListOperations<String, Price> listOperations = (ListOperations<String, Price>) redisTemplate.opsForList();
        // 随机到要抽中的奖品索引 = 0 到 当前剩余奖品数
        Long remainTotal = listOperations.size(RedisKeyConstant.DEFAULT_PRICE_POOL);
        log.info("==抽奖开始==");
        int k = 0;
        // 处理随机数异常
        try {
            k = RandomUtil.randomInt(0, Convert.toInt(remainTotal));
        } catch (IllegalArgumentException e) {
            System.err.println("有效奖品已为空,直接返回null");
            return null;
        }
        Price price = convertToPrice(listOperations.index(RedisKeyConstant.DEFAULT_PRICE_POOL, k));
        if (price != null) {
            if (StrUtil.isNotEmpty(price.getName())) {
                log.debug("拿到了一件奖品:{}", price.getName());
                Long key1 = listOperations.remove(RedisKeyConstant.DEFAULT_PRICE_POOL, 1, price);
                log.debug("移除奖品状态:{}", key1 != 0);
                Long remainTotalFinish = listOperations.size(RedisKeyConstant.DEFAULT_PRICE_POOL);
                log.debug("抽奖结束后还剩余:{}件", remainTotalFinish);
                return price;
            } else {
                System.err.println("=手慢了  奖池空了=  随机数为 " + k);
                return null;
            }
        }
        System.out.println("抽奖结束了一次 \n\n\n\n\n");
        return null;
    }


    public Price convertToPrice(Object object) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(object), Price.class);
    }


    // 对redis进行初始化
    @Override
    public void initPriceStock(Set<Price> priceSet) {
        // 清理所有key
        Set<String> keys = redisTemplate.keys("*");
        boolean delete = redisTemplate.delete(keys) != 0;
        log.debug("[清理原数据状态:{}]", delete);
        // 设置当前进行到第几轮了
        strRedisTemplate.opsForValue().set("CURRENT_ROUND", 1 + "");
        ListOperations<String, Price> listOperations = (ListOperations<String, Price>) redisTemplate.opsForList();

        List<Price> list = new ArrayList<>();
        //  初始化大奖
        for (int i = 0; i < priceCount1; i++) {
            list.add(new Price("airPods", "1000", "BIG"));
        }
        for (int i = 0; i < priceCount2; i++) {
            list.add(new Price("huaweiP40", "6000", "BIG"));
        }
        //  初始化保底奖
        for (int i = 0; i < comfortCount; i++) {
            list.add(new Price("编织袋", "10", "SMALL"));
        }
        Long aLong = listOperations.leftPushAll(RedisKeyConstant.DEFAULT_PRICE_POOL, list);
        log.debug("[奖品数据初始化完成:{},配置数据{}条]", aLong != 0, listOperations.size(RedisKeyConstant.DEFAULT_PRICE_POOL));

    }


}
