package cn.yejy;


import cn.yejy.repository.RoleRepository;
import cn.yejy.repository.UserRepository;
import cn.yejy.service.UserService;
import cn.yejy.util.JwtTokenHelper;
import org.jooq.Record;
import org.jooq.Record3;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Test
    public void test() {
        Map<String,Object> users = userService.findUserAndRolesByUsername("yejy");
        System.out.println(users);

    }
}
