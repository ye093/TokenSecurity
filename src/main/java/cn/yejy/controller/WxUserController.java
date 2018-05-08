package cn.yejy.controller;

import cn.yejy.constant.ErrorCode;
import cn.yejy.dao.WxEncryptedData;
import cn.yejy.dao.WxOpenKeyDAO;
import cn.yejy.data.ResponseData;
import cn.yejy.service.OpenUserService;
import cn.yejy.util.JsonUtil;
import cn.yejy.util.TextUtil;
import cn.yejy.util.WxRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/* 微信用户信息*/
@RestController
public class WxUserController {

    @Autowired
    OpenUserService openUserService;

    @PostMapping(value = "/wx/user/fetch")
    public ResponseEntity fetchUserInfo(@RequestBody WxEncryptedData wxEncryptedData,
                                        HttpServletRequest request) {
        WxOpenKeyDAO wxOpenKeyDAO = new WxOpenKeyDAO(request);
        String text = WxRequestUtil.decryptData(wxOpenKeyDAO.getSessionKey(), wxEncryptedData.getEncryptedData(), wxEncryptedData.getIv());
        Map<String, Object> data = JsonUtil.parse(text);
        System.out.println(data);
        // 判断登录的appid与解析出来的appid是否一致
        String parseOpenId = (String) data.get("openId");
        String nickName = (String) data.get("nickName");
        Integer gender = (Integer) data.get("gender");
        String city = (String) data.get("city");
        String province = (String) data.get("province");
        String country = (String) data.get("country");
        String avatarUrl = (String) data.get("avatarUrl");
        if (TextUtil.isEmpty(parseOpenId) || !parseOpenId.equals(wxOpenKeyDAO.getOpenid())) {
            return ResponseData.error(ErrorCode.ERROR_REQUEST, "非法请求");
        }
        Map<String, Object> resData = openUserService.getOrSave(parseOpenId, nickName, gender.byteValue(), city, province, country, avatarUrl);
        return ResponseData.ok("success", resData);
    }
}
