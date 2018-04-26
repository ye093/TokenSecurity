package cn.yejy.service;

import cn.yejy.entity.User;
import cn.yejy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    // 事务例子
    @Transactional(rollbackFor = Exception.class)
    public void myTransactional() throws Exception{
        int result = userRepository.delete(5L);
        if (result != 1) {
            throw new Exception("删除失败");
        }
        System.out.println("删除成功");
        User user = userRepository.findById(4L);
        if (user.getIsEnabled() == 0) {
            System.out.println("还原成功");
            throw new Exception("无权修改失败");
        }
    }

    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> find() {
        return userRepository.findAll();
    }

    public Long save() {
        User user = userRepository.save(new User("177098899713", "叶金耘", "yejinyun", 0, 0, 1, null, new Date()));
        return user.getUserId();
    }

    public int update(Long userId) {
        return userRepository.update(userId, new Date());
    }

    public Integer delete(Long userId) {
        return userRepository.delete(userId);
    }


}
