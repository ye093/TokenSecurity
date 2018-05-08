package cn.yejy;


import cn.yejy.repository.UserRepository;
import cn.yejy.service.SendSmsService;
import cn.yejy.service.UserService;
import cn.yejy.service.WxRequestService;
import cn.yejy.util.JsonUtil;
import cn.yejy.util.WxRequestUtil;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WxRequestService wxRequestService;


    @Test
    public void test() {
        Map object = wxRequestService.login("003oNgba0Rk58v1OyXca0N7iba0oNgbB");
        System.out.println(object);
    }
}
