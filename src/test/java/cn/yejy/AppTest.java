package cn.yejy;


import cn.yejy.service.GoodsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    GoodsService goodsService;

    @Test
    public void test() {
        Map<String, Object> data = goodsService.getGoodsByBarCode("6922255451427");
        System.out.println(data);
    }
}
