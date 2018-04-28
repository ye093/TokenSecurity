package cn.yejy.util;

import javax.servlet.ServletRequest;
import java.util.Map;

public class UserHolderUtil {

    public static void setHolder(Map<String, Object> data, ServletRequest req) {
        Integer userId = (Integer) data.get("user_id");
        String openId = (String) data.get("open_id");
        String mobile = (String) data.get("mobile");
        String username = (String) data.get("username");
        req.setAttribute("userId", userId);
        req.setAttribute("openId", openId);
        req.setAttribute("mobile", mobile);
        req.setAttribute("username", username);
    }

    public static Integer getUserId(ServletRequest req) {
        return (Integer) req.getAttribute("userId");
    }

    public static String getOpenId(ServletRequest req) {
        return (String) req.getAttribute("openId");
    }

    public static String getMobile(ServletRequest req) {
        return (String) req.getAttribute("mobile");
    }

    public static String getUsername(ServletRequest req) {
        return (String) req.getAttribute("username");
    }
}
