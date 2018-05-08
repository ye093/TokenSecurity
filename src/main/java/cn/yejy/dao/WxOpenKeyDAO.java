package cn.yejy.dao;

import javax.servlet.http.HttpServletRequest;

public class WxOpenKeyDAO {

    private String openid;
    private String sessionKey;

    public WxOpenKeyDAO(HttpServletRequest req) {
        this.openid = (String) req.getAttribute("openid");
        this.sessionKey = (String) req.getAttribute("session_key");
    }

    public String getOpenid() {
        return openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }
}
