package cn.yejy.service;

import cn.yejy.jooq.domain.tables.User;
import cn.yejy.repository.RoleRepository;
import cn.yejy.repository.UserRepository;
import org.jooq.Record;
import org.jooq.Record1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    // 事务例子
    @Transactional(rollbackFor = Exception.class)
    public void myTransactional() throws Exception {
        int result = userRepository.delete(5);
        if (result != 1) {
            throw new Exception("删除失败");
        }
        System.out.println("删除成功");
        Record user = userRepository.findById(4);
        boolean isEnable = user.get(User.USER.IS_ENABLED);
        if (isEnable) {
            System.out.println("还原成功");
            throw new Exception("无权修改失败");
        }
    }

    public Record findById(Integer userId) {
        return userRepository.findById(userId);
    }

    public List<Map<String, Object>> find() {
        return userRepository.findAll();
    }

    public Record save(String username, String mobile, String password, boolean isEnable, boolean isExpired, boolean isLocked) {
        Record userDAO = userRepository.save(username, mobile, password, isEnable, isExpired, isLocked);
        return userDAO;
    }

    public int update(Integer userId, String username, String password) {
        return userRepository.update(userId, username, password);
    }

    public Integer delete(Integer userId) {
        return userRepository.delete(userId);
    }

    /**
     * 根据用户名查找用户
     */
    public Map<String, Object> findUserAndRolesByUsername(String username) {
        Map<String, Object> userWithRoles = null;
        Record user = userRepository.findByUsername(username);
        if (user != null) {
            Integer userId = user.getValue(User.USER.USER_ID);
            List<String> roles = roleRepository.getRolesByUserId(userId);
            userWithRoles = user.intoMap();
            if (roles != null && roles.size() > 0) {
                userWithRoles.put("roles", roles);
            }
        }
        return userWithRoles;
    }

}
