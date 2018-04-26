package cn.yejy.repository;

import cn.yejy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "user")
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Cacheable(key = "#userId")
    public User findById(Long userId) {
        BeanPropertyRowMapper<User> userMapper = new BeanPropertyRowMapper<>(User.class);
        //查询单体
        User user = null;
        try {
            user = jdbcTemplate.queryForObject("SELECT user_id AS userId, mobile, username, password, is_expired AS isExpired, is_locked AS isLocked, is_enabled AS isEnabled, updated_time AS updatedTime, created_time AS createdTime FROM user WHERE user_id = ?",
                    userMapper, userId);
        } catch (DataAccessException e) {
            // empty
        }
        return user;
    }

    public List<User> findAll() {
        BeanPropertyRowMapper<User> userMapper = new BeanPropertyRowMapper<>(User.class);
        //查询单体
        List<User> users = jdbcTemplate.query("SELECT user_id AS userId, mobile, username, password, is_expired AS isExpired, is_locked AS isLocked, is_enabled AS isEnabled, updated_time AS updatedTime, created_time AS createdTime FROM user",
                userMapper);
        return users;
    }

    @CachePut(key = "#user.userId") //更新缓存,CachePut会将返回结果集缓存到数据中
    public User save(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int[] types = new int[]{
                Types.VARCHAR,
                Types.VARCHAR,
                Types.VARCHAR,
                Types.INTEGER,
                Types.INTEGER,
                Types.INTEGER,
                Types.TIMESTAMP
        };
        PreparedStatementCreatorFactory pscFactory = new PreparedStatementCreatorFactory("INSERT INTO `user`(mobile,username,`password`,is_expired,is_locked,is_enabled,created_time) VALUES(?,?,?,?,?,?,?)", types);
        pscFactory.setReturnGeneratedKeys(true);
        PreparedStatementCreator psc = pscFactory.newPreparedStatementCreator(new Object[]{user.getMobile(), user.getUsername(), user.getPassword(), user.getIsExpired(),
                user.getIsLocked(), user.getIsEnabled(), user.getCreatedTime()});
        jdbcTemplate.update(psc, keyHolder);
        Long userId = keyHolder.getKey().longValue();
        user.setUserId(userId);
        return user;
    }

    @CacheEvict(key = "#userId") // 更新缓存,CachePut会将返回结果集缓存到数据中
    public Integer update(Long userId, Date updatedTime) {
        int rows = jdbcTemplate.update("UPDATE `user` SET updated_time=? WHERE user_id=?", updatedTime, userId);
        return rows;
    }

    @CacheEvict(key = "#userId") // 移除缓存
    public Integer delete(Long userId) {
        int rows = jdbcTemplate.update("DELETE FROM `user` WHERE user_id=?", userId);
        return rows;
    }

}
