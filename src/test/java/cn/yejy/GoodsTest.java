package cn.yejy;

import cn.yejy.repository.GoodsCodeRepository;
import cn.yejy.repository.GoodsRepository;
import org.jooq.Record;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTest {
    @Autowired
    GoodsRepository repository;

    @Autowired
    GoodsCodeRepository codeRepository;

    @Test
    public void select() {
//        Record record = repository.getGoodsByGoodsCode("25811");
//        Record record = repository.getGoodsByBarCode("6928804011760");
        Record record = repository.getGoodsById(3);
        Assert.assertNotNull(record);
        System.out.println(record);
    }

    @Test
    public void save() {
        Integer id = repository.save("测试商品", "3578855", "瓶", "500ML", "商品", "cssp"
        , "诺基亚", "manu_name", "地址", true, 360,
                "三年质保", "http://www.baidu.com", BigDecimal.valueOf(33.2), BigDecimal.valueOf(63.3),
                false, true, false, "存储", 332, "中国",
                "出太阳", "备注", LocalDateTime.now(), "yejy", LocalDateTime.now(), "jyye");
        System.out.println(id);
    }

    @Test
    public void update() {
        repository.update(5, "商品改", null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null ,null, null, null, null,
                null, null, null, null, null);
    }

    @Test
    public void codeSave() {
        boolean res = codeRepository.save("25810", 1);
        Assert.assertTrue(res);
    }

    @Test
    public void codeUpdate() {
        boolean res = codeRepository.updateGoodsCode("25811", 1);
        Assert.assertTrue(res);
    }
}
