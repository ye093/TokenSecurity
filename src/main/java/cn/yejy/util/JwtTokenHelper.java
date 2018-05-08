package cn.yejy.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenHelper {
    @Value("${token.key}")
    private String tokenKey;
    @Value("${token.issuer}")
    private String tokenIssuer;
    @Value("${token.subject}")
    private String tokenSubject;
    @Value("${token.expiration}")
    private Long tokenExpiration;
    @Value("${token.header}")
    private String headerKey;

    public String getToken(Map<String, Object> data) {
        return getToken(data, tokenExpiration);
    }

    public String getToken(Map<String, Object> data, long expiration) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setIssuer(tokenIssuer)
                .setSubject(tokenSubject)
                .addClaims(data)
                .signWith(SignatureAlgorithm.HS256, tokenKey);
        if (expiration != 0) {
            Date date = new Date(System.currentTimeMillis() + expiration);
            jwtBuilder.setExpiration(date);
        }
        return jwtBuilder.compact();
    }

    public Map<String, Object> parseToken(String token) throws ExpiredJwtException, Exception {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(tokenKey)
                .requireIssuer(tokenIssuer)
                .requireSubject(tokenSubject)
                .parseClaimsJws(token);
        Map<String, Object> data = claims.getBody();
        return data;
    }

    public Map<String, Object> parseToken(HttpServletRequest request) throws ExpiredJwtException,Exception{
        String token = request.getHeader(headerKey);
        if (token == null || TextUtil.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(tokenKey)
                .requireIssuer(tokenIssuer)
                .requireSubject(tokenSubject)
                .parseClaimsJws(token);
        Map<String, Object> data = claims.getBody();
        return data;
    }
}
