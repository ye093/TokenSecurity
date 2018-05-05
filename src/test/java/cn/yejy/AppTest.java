package cn.yejy;


import cn.yejy.repository.UserRepository;
import cn.yejy.service.SendSmsService;
import cn.yejy.service.UserService;
import cn.yejy.util.JsonUtil;
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
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SendSmsService sendSmsService;


    @Test
    public void test() {
         Map<String, Object> data = JsonUtil.parse("{\"phone\":\"222\"}");
        System.out.println(data);
    }

    @Test
    public void serviceTest() {
        boolean result = sendSmsService.register("13630159257", "003214");
        Assert.assertTrue(result);
    }
}
