package cn.yejy.repository;

import cn.yejy.jooq.domain.tables.Role;
import cn.yejy.jooq.domain.tables.UserRoleRel;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleRepository {
    @Autowired
    DSLContext dsl;

    public List<String> getRolesByUserId(Integer userId) {
        Role r = Role.ROLE.as("r");
        UserRoleRel ur = UserRoleRel.USER_ROLE_REL.as("ur");
        List<String> roles = dsl.select(r.ROLE_).from(r).innerJoin(ur).on(r.ROLE_ID.eq(ur.ROLE_ID))
                 .where(ur.USER_ID.eq(userId).and(r.IS_ENABLED.isTrue())).fetch(r.ROLE_);
        return roles;
    }
}
