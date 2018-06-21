package cn.yejy.constant;


public class ErrorCode {
    // 用户不存在
    public static final Integer USER_NOT_EXISTS = 4001;
    // 认证失败
    public static final Integer AUTHORIZE_FAILURE = 4002;
    // 打不到相关记录
    public static final Integer RECORD_EMPTY = 4003;
    // 请求参数出错
    public static final Integer PARAM_ERROR = 4004;
    // 认证过期
    public static final Integer TOKEN_EXPIRED = 4005;
    // 非法请求
    public static final Integer ERROR_REQUEST = 4006;
}
