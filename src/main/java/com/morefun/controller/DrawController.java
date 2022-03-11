package com.morefun.controller;

import com.morefun.common.constant.ErrorCode;
import com.morefun.common.constant.MethodResult;
import com.morefun.common.constant.RedisKeyConstant;
import com.morefun.common.thread.UserThreadLocal;
import com.morefun.domain.Price;
import com.morefun.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Song
 * @date 2022/3/10 10:15
 * @Version 1.0
 */
@RestController
@RequestMapping("draw")
public class DrawController {
    @Qualifier("strRedisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PriceService priceService;

    /**
     * 初始化奖品和默认第一轮抽奖
     *
     * @return
     */
    @GetMapping("/init")
    public MethodResult inti() {
        priceService.initPriceStock(null);
        return MethodResult.success(null, "数据已配置");
    }

    /**
     * 切换场次
     *
     * @return
     */
    @GetMapping("/nextRound")
    public MethodResult nextRound() {
        Boolean aBoolean = redisTemplate.opsForValue().setIfPresent("CURRENT_ROUND", 2+"");
        return aBoolean == true ? MethodResult.success(null, "已切换") : MethodResult.failure(ErrorCode.data_not_found, "切换不成功,可能由于未初始化而导致");
    }

    @GetMapping("/doDraw")
    public MethodResult doDraw() {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        String identity = UserThreadLocal.getUser();
        // 判断有没有重复点击
        String current = RedisKeyConstant.buildCurrentDrawRecordKey(identity);
        if (redisTemplate.hasKey(current)) {
            return MethodResult.failure(ErrorCode.no_operation_authority, "当前场次参与过了,不要重复点击");
        }

        // 判断有没有在之前中过大奖
        String cacheIdentityKey = RedisKeyConstant.buildPreDrawRecordKey(identity);
        String cacheIdentityValue = (String) valueOperations.get(cacheIdentityKey);
        if ("BIG".equals(cacheIdentityValue)) {
            return MethodResult.failure(ErrorCode.data_was_used, "由于中过大奖,不可重复中奖");
        }


        Price price = priceService.doDraw();
        if (price != null) {
            valueOperations.set(current, price.getType());
            System.out.println("用户" + identity + "中奖了,已添加缓存");
            return MethodResult.success(price);
        }
        return MethodResult.failure(ErrorCode.data_was_used, "没抽中");
    }
}
