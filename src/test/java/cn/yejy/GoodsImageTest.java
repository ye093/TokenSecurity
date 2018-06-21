package cn.yejy;

import cn.yejy.repository.GoodsImageRepository;
import org.jooq.Result;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsImageTest {
    @Autowired
    GoodsImageRepository repository;

    @Test
    public void save() {
        boolean res =  repository.save(1, (byte)2, (byte)2, "222", LocalDateTime.now(), "yejy");
        Assert.assertTrue(res);
    }

    @Test
    public void update() {
        boolean res = repository.update(4L, null, (byte) 2, null, "2222", LocalDateTime.now(), "yejy");
        Assert.assertTrue(res);
    }

    @Test
    public void select() {
        Result res = repository.getByGoodsId(1, (byte) 2);
        Assert.assertNotNull(res);
        System.out.println(res);
    }

    @Test
    public void delete() {
        boolean res = repository.deleteById(3L);
        Assert.assertTrue(res);
    }
}
