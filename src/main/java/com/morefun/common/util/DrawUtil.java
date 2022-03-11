package com.morefun.common.util;

import cn.hutool.core.convert.Convert;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jca.context.SpringContextResourceAdapter;
import org.springframework.stereotype.Component;

/**
 * @author Song
 * @date 2022/3/11 11:53
 * @Version 1.0
 */
@Component
public class DrawUtil {
    /**
     * 查询当前进行到第几轮了
     * @return
     */
    public static Integer getCurrentRound() {
        RedisTemplate redisTemplate = SpringContextHolder.getBean("strRedisTemplate",RedisTemplate.class);
        Object o = redisTemplate.opsForValue().get("CURRENT_ROUND");
        return Convert.toInt(o);
    }
}
