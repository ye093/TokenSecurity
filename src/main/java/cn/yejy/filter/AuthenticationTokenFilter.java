package cn.yejy.filter;

import cn.yejy.constant.RoleConstant;
import cn.yejy.util.JwtTokenHelper;
import cn.yejy.util.UserHolderUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Map<String, Object> data;
        try {
            data = jwtTokenHelper.parseToken((HttpServletRequest) req);
        } catch (ExpiredJwtException e) {
            Map<String,Object> resData = new LinkedHashMap<>();
            resData.put("code", 403);
            resData.put("msg", "认证过期");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().print(JSONObject.toJSONString(resData));
            return;
        } catch (Exception e) {
            Map<String,Object> resData = new LinkedHashMap<>();
            resData.put("code", 403);
            resData.put("msg", "认证失败");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().print(JSONObject.toJSONString(resData));
            return;
        }
        if (data != null) {
            String username = (String) data.get("username");
            List<String> roles = (List<String>) data.get("roles");
            List<GrantedAuthority> authorities = null;
            if (roles != null) {
                authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            }
            if (authorities == null || authorities.size() == 0) {
                // 没分配权限，表示是一般会员
                authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(RoleConstant.USER);
            }
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(username, "N/A", authorities));

            // 放在一次请求中
            UserHolderUtil.setHolder(data, req);
        }
        chain.doFilter(req, res);
    }
}
