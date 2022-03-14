import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.song.LuckDrawApplication;
import com.song.domain.Price;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author Song
 * @date 2022/3/10 13:52
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(classes = LuckDrawApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {
    @Qualifier("objRedisTemplate")
    @Autowired
    private RedisTemplate<String, ? extends Object> redisTemplate;
    // 奖品1 数量
    int priceCount1 = 100;
    // 奖品2 数量
    int priceCount2 = 50;

    @Test
    public void test() {
        ListOperations<String, Price> listOperations = (ListOperations<String, Price>) redisTemplate.opsForList();

        List<Price> list = new ArrayList<>();
        Price price1 = new Price("airPods", "1000","BIG");
        Price price2 = new Price("huaweiP40", "6000","BIG");

        list.add(price1);
        list.add(price2);

        Long aLong = listOperations.leftPushAll("key1", list);
        System.out.println("存数据后长度" + listOperations.size("key1"));
        System.out.println("========");
        Object price = listOperations.rightPop("key1");
        Price price3 = JSONUtil.toBean(JSONUtil.toJsonStr(price), Price.class);
        System.out.println(price3);
        System.out.println("取数据后长度" + listOperations.size("key1"));

    }


    @Before   // 放奖品
    public void initRedis() {
        Set<String> keys = redisTemplate.keys("key" + "*");
        boolean delete = redisTemplate.delete(keys) != 0;
        System.out.println("[清理原数据状态]" + delete);


        ListOperations<String, Price> listOperations = (ListOperations<String, Price>) redisTemplate.opsForList();


        List<Price> list = new ArrayList<>();
        for (int i = 0; i < priceCount1; i++) {
            list.add(new Price("airPods", "1000","BIG"));
        }
        for (int i = 0; i < priceCount2; i++) {
            list.add(new Price("huaweiP40", "6000","BIG"));
        }

        Long aLong = listOperations.leftPushAll("key1", list);
        System.out.println("[数据初始化完成]" + listOperations.size("key1"));

       /* Object price = listOperations.rightPop("key1");
        Price price3 = JSONUtil.toBean(JSONUtil.toJsonStr(price), Price.class);
        System.out.println(price3);
        System.out.println("取数据后长度" + listOperations.size("key1"));*/

    }

    @Test   // 开始抽奖
    public void lottery() {
        ListOperations<String, Price> listOperations = (ListOperations<String, Price>) redisTemplate.opsForList();
        // 随机到要抽中的奖品索引
        for (int i = 0; i < 151; i++) {
            log.info("==第{}次抽奖开始了==", i);
            int k = RandomUtil.randomInt(0, priceCount1 + priceCount2 - i+1);
            log.info("==当前随机数为{}==", k);
            Price price = convertToPrice(listOperations.index("key1", k));
            if (price != null) {
                if (StrUtil.isNotEmpty(price.getName())) {
                    System.out.println("拿到了一件奖品:" + price.getName());
                    Long key1 = listOperations.remove("key1", 1, price);
                    System.out.println("移除奖品状态" + (key1 != 0));
                    Long remainTotal = listOperations.size("key1");
                    System.out.println("抽奖结束后还剩余:" + remainTotal + "件");
                } else {
                    System.out.println("=手慢了  奖池空了=  随机数为 " + k);
                }
            }
            System.out.println("结束了一次 \n\n\n\n\n");
        }

    }


    public Price convertToPrice(Object object) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(object), Price.class);
    }

    @Test
    public void testRandom() {
        ListOperations<String, Price> listOperations = (ListOperations<String, Price>) redisTemplate.opsForList();
        // 随机到要抽中的奖品索引
        for (int i = 0; i < 151; i++) {
            log.info("==第{}次抽奖开始了==", i);
            int k = RandomUtil.randomInt(0, priceCount1 + priceCount2 - i+1);
            log.info("==当前随机数为{}==", k);
            Price price = convertToPrice(listOperations.index("key1", k));
            if (price != null) {
                if (StrUtil.isNotEmpty(price.getName())) {
                    System.out.println("拿到了一件奖品:" + price.getName());
                    Long key1 = listOperations.remove("key1", 1, price);
                    System.out.println("移除奖品状态" + (key1 != 0));
                    Long remainTotal = listOperations.size("key1");
                    System.out.println("抽奖结束后还剩余:" + remainTotal + "件");
                } else {
                    System.out.println("=手慢了  奖池空了=  随机数为 " + k);
                }
            }
            System.out.println("结束了一次 \n\n\n\n\n");
        }

    }
}
