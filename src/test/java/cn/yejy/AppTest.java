package cn.yejy;


import cn.yejy.entity.User;
import cn.yejy.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Test
    public void test() {
        User user = userRepository.findById(6L);

        System.out.println("user: " + user);
    }
}
