package cn.yejy.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;

/**
 * 微信请求方法集
 */
public class WxRequestUtil {
    private final static String BASE_URL = "https://api.weixin.qq.com";

    // 登录凭证校验
    private final static String AUTHORIZATION = "/sns/jscode2session";

    /**
     * 登录凭证校验
     */
    public static String jscode2sessionUrl(String appid, String secret, String js_code) {
        String tempUrl = BASE_URL + AUTHORIZATION
                + "?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
        return String.format(tempUrl, appid, secret, js_code);
    }

    /**
     * 解析微信返回数据
     * @param sessionKey
     * @param encryptedData 被加密的数据
     * @param iv
     * @return
     */
    public static String decryptData(String sessionKey,  String encryptedData,  String iv) {
        if (sessionKey.length() != 24 || iv.length() != 24) {
            return null;
        }
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
