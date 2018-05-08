package cn.yejy.repository;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.yejy.jooq.domain.tables.OpenUser.OPEN_USER;

@Service
public class OpenUserRepository {
    @Autowired
    DSLContext dsl;

    /**
     * 查找用户
     */
    public Record findByOpenid(String openid) {
        return dsl.select().from(OPEN_USER).where(OPEN_USER.OPENID.eq(openid)).fetchOne();
    }

    /**
     * 新增用户
     */
    public Record save(String openid, String nickName, Byte gender, String city, String province, String country, String avatarUrl) {
        LocalDateTime createdTime = LocalDateTime.now();
        Record user = dsl.insertInto(OPEN_USER, OPEN_USER.OPENID, OPEN_USER.NICK_NAME, OPEN_USER.GENDER, OPEN_USER.CITY,
                OPEN_USER.PROVINCE, OPEN_USER.COUNTRY, OPEN_USER.AVATAR_URL, OPEN_USER.CREATED_TIME)
                .values(openid, nickName, gender, city, province, country, avatarUrl, createdTime).returning().fetchOne();
        return user;
    }


}
