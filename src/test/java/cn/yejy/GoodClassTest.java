package cn.yejy;

import cn.yejy.repository.GoodsClassRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodClassTest {
    @Autowired
    GoodsClassRepository classRepository;

    @Test
    public void save() {
        boolean res = classRepository.save(4, (byte) 1, true, 1, "进口苹果", "http://www.baidu.com",
                 LocalDateTime.now(), "yejy");
        System.out.println(res);
        Assert.assertTrue(res);
    }

    @Test
    public void update() {
        boolean res =  classRepository.update(4, false, null, null, null,
                LocalDateTime.now(), "yejy");
        Assert.assertTrue(res);
    }

    @Test
    public void addGoods() throws SQLException {
        boolean res = classRepository.saveGoodsClassRel(new Integer[] { 1, 2}, 4);
        Assert.assertTrue(res);
    }

    @Test
    public void removeGoods() throws SQLException {
        boolean res = classRepository.deleteGoodsClassRel(new Integer[] {1, 2}, 4);
        Assert.assertTrue(res);
    }

    @Test
    public void removeClass() throws SQLException {
        boolean res = classRepository.deleteGoodsClass(4, true);
        Assert.assertTrue(res);
    }
}
