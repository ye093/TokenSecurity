package cn.yejy.controller;

import cn.yejy.constant.ErrorCode;
import cn.yejy.data.ResponseData;
import cn.yejy.service.SendSmsService;
import cn.yejy.service.UserService;
import cn.yejy.util.JsonUtil;
import cn.yejy.util.JwtTokenHelper;
import cn.yejy.util.RandomCodeUtil;
import cn.yejy.util.TextUtil;
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
        String mobile = null;
        if (TextUtil.isNotEmpty(body)) {
            mobile = (String) JsonUtil.parse(body).get("mobile");
        }
        return getSms(mobile, 1);
    }

    /* 重置密码短信*/
    @PostMapping(value = "/sms/resetpwd")
    public ResponseEntity resetPassword(@RequestBody String body) {
        String mobile = null;
        if (TextUtil.isNotEmpty(body)) {
            mobile = (String) JsonUtil.parse(body).get("mobile");
        }
        return getSms(mobile, 3);
    }

    /* 获取短信{type:1登录，2注册，3重置}*/
    private ResponseEntity getSms(String mobile, int type) {
        String errorMsg;

        boolean exists = true;
        if (type == 1 || type == 3) {
            exists = userService.exists(mobile);
        }
        if (exists) {
            String code = RandomCodeUtil.numberSequence(6);
            boolean sendRes = false;
            if (type == 1) {
                sendRes = sendSmsService.login(mobile, code);
            } else if (type == 2) {
                sendRes = sendSmsService.register(mobile, code);
            } else if (type == 3) {
                sendRes = sendSmsService.resetPassword(mobile, code);
            }
            if (sendRes) {
                // 生成短信认证token,时效5分钟
                Map<String, Object> data = new HashMap<>();
                data.put("code", code);
                data.put("mobile", mobile);
                String token = jwtTokenHelper.getToken(data,300000L);

                Map<String, Object> resData = new HashMap<>();
                resData.put("token", token);
                return ResponseData.ok("短信已发送，请您把收到的验证码填写到对应的输入框中", resData);
            } else {
                errorMsg = "短信发送失败，请稍后再试！";
            }
        } else {
            errorMsg = "该用户尚未注册！";
        }
        return ResponseData.error(ErrorCode.USER_NOT_EXISTS, errorMsg);
    }

}
