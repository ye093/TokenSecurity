package cn.yejy.repository;

import static cn.yejy.jooq.domain.tables.User.*;

import cn.yejy.jooq.domain.tables.Role;
import cn.yejy.jooq.domain.tables.User;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "user")
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DSLContext dsl;

    @Cacheable(key = "#userId")
    public Record findById(Integer userId) {
        Record user = dsl.select(USER.USER_ID, USER.USERNAME, USER.MOBILE, USER.IS_ENABLED, USER.IS_EXPIRED, USER.IS_LOCKED, USER.CREATED_TIME, USER.UPDATED_TIME)
                .from(USER).where(USER.USER_ID.eq(userId)).fetchOne();
        return user;
    }

    public List<Map<String, Object>> findAll() {
        List<Map<String, Object>> users = dsl.select(USER.USER_ID, USER.USERNAME, USER.MOBILE, USER.IS_ENABLED, USER.IS_EXPIRED, USER.IS_LOCKED, USER.CREATED_TIME, USER.UPDATED_TIME)
                .from(USER).fetch().intoMaps();
        return users;
    }

//    @CachePut(key = "#userId") //更新缓存,CachePut会将返回结果集缓存到数据中
    public Record save(String username, String mobile, String password, boolean isEnable, boolean isExpired, boolean isLocked) {
        LocalDateTime createdTime = LocalDateTime.now();
        Record user = dsl.insertInto(USER, USER.USERNAME, USER.MOBILE, USER.PASSWORD, USER.IS_ENABLED, USER.IS_EXPIRED, USER.IS_LOCKED, USER.CREATED_TIME, USER.UPDATED_TIME)
                .values(username, mobile, password, isEnable, isExpired, isLocked, createdTime, null).returning().fetchOne();
        return user;
    }

    @CacheEvict(key = "#userId") // 更新缓存,CachePut会将返回结果集缓存到数据中
    public Integer update(Integer userId, String username, String password) {
        LocalDateTime updatedTime = LocalDateTime.now();
        int rows = dsl.update(USER).set(USER.USERNAME, username).set(USER.PASSWORD, password).set(USER.UPDATED_TIME, updatedTime).where(USER.USER_ID.eq(userId)).execute();
        return rows;
    }

    @CacheEvict(key = "#userId") // 移除缓存
    public Integer delete(Integer userId) {
        int rows = dsl.delete(USER).where(USER.USER_ID.eq(userId)).execute();
        return rows;
    }

    /**
     * 根据用户名查找用户
     */
    public Record findByUsername(String username) {
        User u = USER.as("u");
        Role r = Role.ROLE.as("r");
        Record user = dsl.select(u.USER_ID, u.USERNAME, u.IS_LOCKED, u.IS_EXPIRED, u.IS_ENABLED, u.MOBILE, u.PASSWORD, u.OPEN_ID, r.ROLE_, r.NAME.as("role_name"))
                .from(u).innerJoin(r).on(u.ROLE_ID.eq(r.ROLE_ID)).where(u.USERNAME.eq(username)).fetchOne();
        return user;
    }

    /**
     * 根据手机号检查该用户是否存在
     */
    public Record findUserByMobile(String mobile) {
        User u = USER.as("u");
        Role r = Role.ROLE.as("r");
        Record user = dsl.select(u.USER_ID, u.USERNAME, u.IS_LOCKED, u.IS_EXPIRED, u.IS_ENABLED, u.MOBILE, u.PASSWORD, u.OPEN_ID, r.ROLE_, r.NAME.as("role_name"))
                .from(u).innerJoin(r).on(u.ROLE_ID.eq(r.ROLE_ID)).where(u.MOBILE.eq(mobile)).fetchOne();
        return user;
    }

    /**
     * 根据手机号修改密码
     */
    public boolean updatePasswordByMobile(String mobile, String password) {
        int rows = dsl.update(USER).set(USER.PASSWORD, password).where(USER.MOBILE.eq(mobile)).execute();
        return rows == 1;
    }

}
