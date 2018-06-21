package cn.yejy.dao;

public class BasicAuthorDAO {
    private String username;
    private String password;
    // 自动登录
    private Boolean autoLogin;
    // 记住密码
    private Boolean remember;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
