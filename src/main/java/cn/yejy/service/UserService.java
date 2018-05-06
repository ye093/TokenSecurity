package cn.yejy.service;

import cn.yejy.jooq.domain.tables.User;
import cn.yejy.repository.UserRepository;
import cn.yejy.util.TextUtil;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

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
        Record user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return user.intoMap();
    }

    /**
     * 根据手机号查找该用户是否存在
     */
    public boolean exists(String mobile) {
        if (TextUtil.isEmpty(mobile)) {
            return false;
        }
        Record user = userRepository.findUserByMobile(mobile);
        boolean exists = false;
        if (user != null) {
            exists = true;
        }
        return exists;
    }

    public Map<String, Object> findByMobile(String mobile) {
        Record user = userRepository.findUserByMobile(mobile);
        if (user == null) return null;
        return user.intoMap();
    }

    public boolean updatePasswordByMobile(String mobile, String password) {
        if (TextUtil.isAllNotEmpty(mobile, password)) {
            return userRepository.updatePasswordByMobile(mobile, password);
        }
        return false;
    }

}
