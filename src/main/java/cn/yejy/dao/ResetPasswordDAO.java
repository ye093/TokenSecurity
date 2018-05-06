package cn.yejy.dao;

public class ResetPasswordDAO {
    private String mobile;
    private String password;
    private String captcha;
    private String token;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "ResetPasswordDAO{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                '}';
    }
}
