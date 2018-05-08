package cn.yejy.controller;

import cn.yejy.constant.ErrorCode;
import cn.yejy.constant.RoleConstant;
import cn.yejy.dao.BasicAuthorDAO;
import cn.yejy.dao.ResetPasswordDAO;
import cn.yejy.dao.SmsDAO;
import cn.yejy.data.ResponseData;
import cn.yejy.service.UserService;
import cn.yejy.service.WxRequestService;
import cn.yejy.util.JwtTokenHelper;
import cn.yejy.util.TextUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class AuthorizeController {
    @Autowired
    JwtTokenHelper tokenHelper;
    @Autowired
    UserService userService;
    @Autowired
    WxRequestService wxRequestService;

    /* 账号密码登录*/
    @PostMapping(value = "/authorize/base")
    public ResponseEntity authorizeBase(@RequestBody BasicAuthorDAO baseUserPwd) {
        Map<String, Object> user = userService.findUserAndRolesByUsername(baseUserPwd.getUsername());
        if (user == null || !user.get("password").equals(baseUserPwd.getPassword())) {
            return ResponseData.error(ErrorCode.AUTHORIZE_FAILURE, "用户名或密码错误");
        }
        // 认证通过
        String token = tokenHelper.getToken(user);
        return ResponseData.ok("success", userToClient(user, token));
    }

    /* 短信登录*/
    @PostMapping(value = "/authorize/sms")
    public ResponseEntity authorizeSms(@RequestBody SmsDAO smsDAO) {
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
            String reqCode = smsDAO.getCaptcha();
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
                    return ResponseData.ok("success", userToClient(user, token));
                }
            } else {
                errorMsg = "验证码错误";
            }
        }
        return ResponseData.error(ErrorCode.PARAM_ERROR, errorMsg);
    }

    /* 小程序登录*/
    @PostMapping(value = "/authorize/wx")
    public ResponseEntity authorizeWx(@RequestParam("js_code") String jsCode) {
        Map resObj =  wxRequestService.login(jsCode);
        if (resObj == null) {
            // 重试一次
            resObj =  wxRequestService.login(jsCode);
        }
        if (resObj != null) {
            // 提取
            String openid = (String) resObj.get("openid");
            String sessionKey = (String) resObj.get("session_key");
            // 用户权限
            String role = RoleConstant.USER;
            if (TextUtil.isAllNotEmpty(openid, sessionKey)) {
                Map<String, Object> tokenData = new HashMap<>();
                tokenData.put("openid", openid);
                tokenData.put("session_key", sessionKey);
                tokenData.put("role", role);
                String token = tokenHelper.getToken(tokenData);
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                return ResponseData.ok("success", data);
            }
        }
        // 认证失败
        return ResponseData.error(ErrorCode.AUTHORIZE_FAILURE, "登录失败");
    }

    private Map<String, Object> userToClient(Map<String, Object> user, String token) {
        Map<String, Object> linkedMap = new LinkedHashMap<>();
        linkedMap.put("username", user.get("username"));
        linkedMap.put("role", user.get("role"));
        linkedMap.put("roleName", user.get("role_name"));
        linkedMap.put("token", token);
        return linkedMap;
    }

    /* 短信重置密码*/
    @PostMapping(value = "/authorize/resetpwd")
    public ResponseEntity authorizeReset(@RequestBody ResetPasswordDAO passwordDAO) {
        String errorMsg = null;
        String accessToken = passwordDAO.getToken();
        Map<String, Object> reqData = null;
        if (accessToken == null) {
            errorMsg = "请先获取验证码";
        } else {
            try {
                reqData = tokenHelper.parseToken(accessToken);
            } catch (ExpiredJwtException e) {
                errorMsg = "验证码已失效，请重新获取";
            } catch (Exception e) {
                errorMsg = "请先获取验证码";
            }
        }
        if (reqData != null) {
            String reqCode = passwordDAO.getCaptcha();
            String mobile = passwordDAO.getMobile();
            String parseCode = (String) reqData.get("code");
            String parseMobile = (String) reqData.get("mobile");
            // 验证通过
            String password = passwordDAO.getPassword();
            if (TextUtil.isAllNotEmpty(reqCode, parseCode, parseMobile, password)
                    && reqCode.equals(parseCode) && mobile.equals(parseMobile)) {

                boolean result = userService.updatePasswordByMobile(mobile, password);
                if (result) {
                    return ResponseData.ok("重置密码成功，请使用新密码登录");
                } else {
                    errorMsg = "重置密码失败，请联系管理员";
                }
            } else {
                errorMsg = "验证码错误";
            }
        }
        return ResponseData.error(ErrorCode.PARAM_ERROR, errorMsg);
    }




}
