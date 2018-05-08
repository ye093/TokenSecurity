package cn.yejy.filter;

import cn.yejy.constant.ErrorCode;
import cn.yejy.util.JwtTokenHelper;
import cn.yejy.util.TextUtil;
import cn.yejy.util.UserHolderUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.jooq.tools.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
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
            resData.put("code", ErrorCode.TOKEN_EXPIRED);
            resData.put("msg", "登录过期，请重新登录");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().print(JSONObject.toJSONString(resData));
            return;
        } catch (Exception e) {
            Map<String,Object> resData = new LinkedHashMap<>();
            resData.put("code", ErrorCode.ERROR_REQUEST);
            resData.put("msg", "非法请求");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().print(JSONObject.toJSONString(resData));
            return;
        }
        if (data != null) {
            String openid = (String) data.get("openid");
            String session_key = (String) data.get("session_key");
            String role = (String) data.get("role");
            if (TextUtil.isAllNotEmpty(openid, session_key)) {
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(openid, "N/A", authorities));
                // 放在一次请求中
                req.setAttribute("openid", openid);
                req.setAttribute("session_key", session_key);
            } else {
                String username = (String) data.get("username");
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(username, "N/A", authorities));

                // 放在一次请求中
                UserHolderUtil.setHolder(data, req);
            }
        }
        chain.doFilter(req, res);
    }
}
