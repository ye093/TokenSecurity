package cn.yejy.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SendSmsService {
    private static final Logger log = LoggerFactory.getLogger(SendSmsService.class);

    @Value("${alisms.access-key-id}")
    private String accessKeyId;

    @Value("${alisms.access-key-secret}")
    private String accessKeySecret;

    @Value("${alisms.sms-sign-name}")
    private String smsSignName;

    @Value("${alisms.sms-template-code-reg}")
    private String smsTemplateCodeReg;

    @Value("${alisms.sms-template-code-login}")
    private String smsTemplateCodeLogin;

    @Value("${alisms.sms-template-code-reset}")
    private String smsTemplateCodeReset;

    private boolean send(final String phoneNumber, final String templateCode, final String code) {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(smsSignName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(templateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者 发送回执ID,可根据该ID查询具体的发送状态
//            request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                return true;
            }
        } catch (ClientException e) {
            log.error("短信发送失败,原因：%s", e.getErrMsg());
        }
        return false;
    }

    /**
     * 发送登录短信
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 成功或失败标识
     */
    public boolean login(final String phoneNumber, final String code) {
        return send(phoneNumber, smsTemplateCodeLogin, code);
    }

    /**
     * 发送注册短信
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 成功或失败标识
     */
    public boolean register(final String phoneNumber, final String code) {
        return send(phoneNumber, smsTemplateCodeReg, code);
    }

    /**
     * 发送重置密码短信
     * @param phoneNumber 手机号码
     * @param code 验证码
     * @return 成功或失败标识
     */
    public boolean resetPassword(final String phoneNumber, final String code) {
        return send(phoneNumber, smsTemplateCodeReset, code);
    }
}
