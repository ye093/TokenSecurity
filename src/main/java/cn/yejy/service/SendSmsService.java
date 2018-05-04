package cn.yejy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendSmsService {
    @Value("${token.issuer}")
    private String sms;

}
