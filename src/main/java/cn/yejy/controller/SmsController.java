package cn.yejy.controller;

import cn.yejy.constant.ErrorCode;
import cn.yejy.data.ResponseData;
import cn.yejy.service.SendSmsService;
import cn.yejy.service.UserService;
import cn.yejy.util.JsonUtil;
import cn.yejy.util.JwtTokenHelper;
import cn.yejy.util.RandomCodeUtil;
import cn.yejy.util.TextUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/* 短信相关*/
@RestController
public class SmsController {
    @Autowired
    UserService userService;

    @Autowired
    SendSmsService sendSmsService;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    /* 登录短信*/
    @PostMapping(value = "/sms/login")
    public ResponseEntity smsLogin(@RequestBody String body) {
        String phone = null;
        if (TextUtil.isNotEmpty(body)) {
            phone = (String) JsonUtil.parse(body).get("phone");
        }
        System.out.println(phone + "->4444");
        String errorMsg;
        boolean exists = userService.exists(phone);
        if (exists) {
            String code = RandomCodeUtil.numberSequence(6);
            boolean sendRes = sendSmsService.login(phone, code);
            if (sendRes) {
                // 生成短信认证token,时效5分钟
                Map<String, Object> data = new HashMap<>();
                data.put("code", code);
                data.put("mobile", phone);
                String token = jwtTokenHelper.getToken(data,300000L);

                Map<String, Object> resData = new HashMap<>();
                resData.put("token", token);
                return ResponseData.ok("success", resData);
            } else {
                errorMsg = "短信发送失败，请稍后再试！";
            }
        } else {
            errorMsg = "该用户尚未注册！";
        }
        return ResponseData.error(ErrorCode.USER_NOT_EXISTS, errorMsg);
    }
}
