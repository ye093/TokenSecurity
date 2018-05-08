package cn.yejy.service;

import cn.yejy.util.JsonUtil;
import cn.yejy.util.WxRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 微信请求服务
 */
@Service
public class WxRequestService {
    private final Logger logger = LoggerFactory.getLogger(WxRequestService.class);

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * 获取登录凭证
     */
    public Map login(String js_code) {
        Map resEntity = null;
        try {
            String text =
                    restTemplate.getForObject(WxRequestUtil.jscode2sessionUrl(appid, secret, js_code), String.class);
            System.out.println(text);
            resEntity = JsonUtil.parse(text);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return resEntity;
    }



}
