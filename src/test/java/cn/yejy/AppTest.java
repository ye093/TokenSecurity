package cn.yejy;


import cn.yejy.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    UserService userService;

    @Test
    public void test() {
        try {
            userService.myTransactional();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
