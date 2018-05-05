package cn.yejy.controller;

import cn.yejy.constant.ErrorCode;
import cn.yejy.dao.BasicAuthorDAO;
import cn.yejy.dao.SmsDAO;
import cn.yejy.data.ResponseData;
import cn.yejy.service.UserService;
import cn.yejy.util.JwtTokenHelper;
import cn.yejy.util.TextUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthorizeController {
    @Autowired
    JwtTokenHelper tokenHelper;
    @Autowired
    UserService userService;

    /* 账号密码登录*/
    @PostMapping(value = "/authorize/base")
    public ResponseEntity authorizeBase(@RequestBody BasicAuthorDAO baseUserPwd) {
        Map<String, Object> user = userService.findUserAndRolesByUsername(baseUserPwd.getUsername());
        if (user == null || !user.get("password").equals(baseUserPwd.getPassword())) {
            return ResponseData.error(ErrorCode.AUTHORIZE_FAILURE, "认证失败");
        }
        // 认证通过
        String token = tokenHelper.getToken(user);
        return ResponseData.ok("success", token);
    }

    /* 短信登录*/
    @PostMapping(value = "/authorize/sms")
    public ResponseEntity authorizeSms(@RequestBody SmsDAO smsDAO) {
        System.out.println(smsDAO);
        String errorMsg = null;
        String accessToken = smsDAO.getToken();
        Map<String, Object> reqData = null;
        if (accessToken == null) {
            errorMsg = "参数错误";
        } else {
            try {
                reqData = tokenHelper.parseToken(accessToken);
            } catch (ExpiredJwtException e) {
                errorMsg = "验证码已失效，请重新获取";
            } catch (Exception e) {
                errorMsg = "参数错误";
            }
        }
        if (reqData != null) {
            String reqCode = smsDAO.getCode();
            String mobile = smsDAO.getMobile();
            String parseCode = (String) reqData.get("code");
            String parseMobile = (String) reqData.get("mobile");
            if (TextUtil.isAllNotEmpty(reqCode, parseCode, parseMobile)
                    && reqCode.equals(parseCode) && mobile.equals(parseMobile)) {
                // 验证通过
                Map<String, Object> user = userService.findByMobile(mobile);
                if (user != null) {
                    // 认证通过
                    String token = tokenHelper.getToken(user);
                    return ResponseData.ok("success", token);
                }
            } else {
                errorMsg = "验证码错误";
            }
        }
        return ResponseData.error(ErrorCode.PARAM_ERROR, errorMsg);
    }

}
