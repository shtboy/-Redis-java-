package com.song.service;

import com.song.domain.Price;

import java.util.Set;

/**
 * @author Song
 * @date 2022/3/10 16:54
 * @Version 1.0
 */
public interface PriceService {
    // 抽奖接口
    Price doDraw();
    void initPriceStock(Set<Price> priceSet);
}
