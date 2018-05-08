package cn.yejy.service;

import cn.yejy.repository.OpenUserRepository;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenUserService {
    @Autowired
    OpenUserRepository openUserRepository;

    public Map<String, Object> getOrSave(String openid, String nickName, Byte gender, String city,
                                         String province, String country, String avatarUrl) {
        Record record = openUserRepository.findByOpenid(openid);
        if (record == null || record.size() == 0) {
            record = openUserRepository.save(openid, nickName, gender, city, province, country, avatarUrl);
        }
        if (record == null) {
            return null;
        }
        Map<String, Object> userMap = record.intoMap();
        return  userMap;
    }

}
