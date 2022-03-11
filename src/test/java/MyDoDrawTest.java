import com.morefun.LuckDrawApplication;
import com.morefun.domain.Price;
import com.morefun.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Song
 * @date 2022/3/10 17:29
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(classes = LuckDrawApplication.class)
@RunWith(SpringRunner.class)
public class MyDoDrawTest {
    @Autowired
    private PriceService priceService;

    @Before
    public void init() {
        priceService.initPriceStock(null);
    }

    @Test
    public void myDoDraw() {

        int count = 0;
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            for (int i = 0; i < 151; i++) {
                Price price = priceService.doDraw();
                if (price != null) {
                    ++count;
                } else {
                    System.out.println("抽到了空的奖品");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
